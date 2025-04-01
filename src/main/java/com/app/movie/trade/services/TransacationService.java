package com.app.movie.trade.services;

import java.time.LocalDate;
import java.util.Base64;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.app.movie.trade.entities.DealDetailInfo;
import com.app.movie.trade.entities.Investment;
import com.app.movie.trade.entities.PaymentInformation;
import com.app.movie.trade.entities.PaymentResponse;
import com.app.movie.trade.entities.Transaction;
import com.app.movie.trade.entities.TransactionDetails;
import com.app.movie.trade.entities.TransactionStatus;
import com.app.movie.trade.enums.InvestmentStatusEnum;
import com.app.movie.trade.enums.TransactionStatusEnum;
import com.app.movie.trade.exceptions.InvalidInvestmentStatusException;
import com.app.movie.trade.exceptions.InvalidTransactionStatusException;
import com.app.movie.trade.repositories.DealRepository;
import com.app.movie.trade.repositories.InvestmentRepository;
import com.app.movie.trade.repositories.TransactionDetailsRepository;
import com.app.movie.trade.repositories.TransactionRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.phonepe.sdk.pg.Env;
import com.phonepe.sdk.pg.payments.v1.PhonePePaymentClient;

@Service
public class TransacationService {
	@Autowired
	TransactionRepository transactionRepository;
	@Autowired
	InvestmentRepository investmentRepository;
	@Autowired
	InvestmentService investmentService;
	@Autowired
	DealService dealService;
	@Autowired
	DealRepository dealRepository;
	@Autowired
	MessageService messageService;
	@Autowired
	TransactionDetailsRepository transactionDetailsRepository;
	Logger logger = LoggerFactory.getLogger(TransacationService.class);

	public Transaction createTransaction(Transaction transaction)
			throws InvalidTransactionStatusException, InvalidInvestmentStatusException {
		if (transaction.getInvestment_id() == null) {
			throw new NoSuchElementException("investment id cannot be null");
		}
		if (investmentRepository.existsById(transaction.getInvestment_id().toUpperCase())) {
			if (TransactionStatusEnum.valueOf(transaction.getStatus()) != null) {
				Investment investment = investmentRepository
						.getReferenceById(transaction.getInvestment_id().toUpperCase());
				transaction.setTransaction_date(LocalDate.now());
				investment.setStatus(InvestmentStatusEnum.ONGOING.name());
				dealService.updateDealStatus(true, investment.getDealid());
				return transaction;
			} else {
				throw new InvalidTransactionStatusException("Invalid transaction status");
			}

		} else {
			throw new NoSuchElementException("no investment found with given id:" + transaction.getInvestment_id());
		}
	}

	public void savePaymentDetails(PaymentResponse pgResponse) throws JsonMappingException, JsonProcessingException {
		logger.info("Updating the transaction details received from phonepe");
		String res = new String(Base64.getDecoder().decode(pgResponse.getResponse()));
		logger.info("decoded response received from phonepe :: " + res);
		ObjectMapper objectMapper = new ObjectMapper();
		PaymentInformation paymentInformation = objectMapper.readValue(res, PaymentInformation.class);
		logger.info("payment information :: " + paymentInformation.toString());
		String transactionId = paymentInformation.getData().getMerchantTransactionId();
		Transaction transaction = transactionRepository.getReferenceById(transactionId);
		transaction.setStatus(paymentInformation.getData().getState());
		transactionRepository.save(transaction);
		updateInvAndDealInfo(transactionId);
		logger.info("updated transaction status for the merchant transaction id :: " + transactionId);
		TransactionDetails tranDetails = convertPaymentInfoToTransDetails(paymentInformation);
		transactionDetailsRepository.save(tranDetails);
		logger.info("saved the transaction details");

	}

	private TransactionDetails convertPaymentInfoToTransDetails(PaymentInformation paymentInformation) {
		TransactionDetails tranDetails = new TransactionDetails();
		if (paymentInformation.getData() != null) {
			tranDetails.setMerchant_id(paymentInformation.getData().getMerchantId());
			tranDetails.setMerchant_transaction_id(paymentInformation.getData().getMerchantTransactionId());
			tranDetails.setTransaction_id(paymentInformation.getData().getTransactionId());
			tranDetails.setAmount(paymentInformation.getData().getAmount() / 100);
			tranDetails.setState(paymentInformation.getData().getState());
			tranDetails.setResponse_code(paymentInformation.getData().getResponseCode());
			if (paymentInformation.getData().getPaymentInstrument() != null) {
				tranDetails.setPayment_type(paymentInformation.getData().getPaymentInstrument().getType());
				tranDetails.setUtr(paymentInformation.getData().getPaymentInstrument().getUtr());
				tranDetails.setIfsc(paymentInformation.getData().getPaymentInstrument().getIfsc());
				tranDetails.setUpi_transaction_id(
						paymentInformation.getData().getPaymentInstrument().getUpiTransactionId());
				tranDetails.setCard_network(paymentInformation.getData().getPaymentInstrument().getCardNetwork());
				tranDetails.setAccount_type(paymentInformation.getData().getPaymentInstrument().getAccountType());
				tranDetails
						.setPg_transaction_id(paymentInformation.getData().getPaymentInstrument().getPgTransactionId());
				tranDetails.setPg_service_transaction_id(
						paymentInformation.getData().getPaymentInstrument().getPgServiceTransactionId());
				tranDetails.setBank_transaction_id(
						paymentInformation.getData().getPaymentInstrument().getBankTransactionId());
				tranDetails.setBank_id(paymentInformation.getData().getPaymentInstrument().getBankId());
				tranDetails.setArn(paymentInformation.getData().getPaymentInstrument().getArn());
				tranDetails.setCard_type(paymentInformation.getData().getPaymentInstrument().getCardType());
				tranDetails.setPg_authorization_code(
						paymentInformation.getData().getPaymentInstrument().getPgAuthorizationCode());
				tranDetails.setUnmasked_account_number(
						paymentInformation.getData().getPaymentInstrument().getUnmaskedAccountNumber());
				tranDetails.setBrn(paymentInformation.getData().getPaymentInstrument().getBrn());
			}
		}
		return tranDetails;
	}

	public void updateInvAndDealInfo(String transactionId) {
		logger.info("updating investment and deal status for given transaction id :: " + transactionId);
		Transaction transaction = transactionRepository.getReferenceById(transactionId);
		String investment_id = transaction.getInvestment_id();
		Investment investment = investmentRepository.getReferenceById(investment_id);
		if (TransactionStatusEnum.valueOf(transaction.getStatus().toUpperCase()) != null) {
			if (transaction.getStatus().toUpperCase().equals(TransactionStatusEnum.COMPLETED.name())) {
				investment.setStatus(InvestmentStatusEnum.ONGOING.name());
				investmentRepository.save(investment);
				logger.info("Investment status updated");
				dealService.updateDealStatus(true, investment.getDealid());
				sendTransMsgToSeller(investment.getDealid());
				logger.info("deal status updated");
			} else if (transaction.getStatus().toUpperCase().equals(TransactionStatusEnum.FAILED.name())
					|| transaction.getStatus().toUpperCase().equals(TransactionStatusEnum.PENDING.name())) {
				investment.setStatus(InvestmentStatusEnum.CANCELLED.name());
				investmentRepository.save(investment);
				logger.info("Investment status updated");
				dealService.updateDealStatus(false, investment.getDealid());
				logger.info("deal status updated");
			}
		}
	}

	public void updateTransactionStatus(TransactionStatus transactionStatus) {
		Transaction transaction = transactionRepository.getReferenceById(transactionStatus.getTransactionId());
		String investment_id = transaction.getInvestment_id();
		Investment investment = investmentRepository.getReferenceById(investment_id);
		if (TransactionStatusEnum.valueOf(transactionStatus.getStatus().toUpperCase()) != null) {
			if (transactionStatus.getStatus().toUpperCase().equals(TransactionStatusEnum.COMPLETED.name())) {
				investment.setStatus(InvestmentStatusEnum.ONGOING.name());
				investmentRepository.save(investment);
				transaction.setStatus(transactionStatus.getStatus().toUpperCase());
				transactionRepository.save(transaction);
				dealService.updateDealStatus(true, investment.getDealid());
			} else if (transactionStatus.getStatus().toUpperCase().equals(TransactionStatusEnum.FAILED.name())
					|| transactionStatus.getStatus().toUpperCase().equals(TransactionStatusEnum.PENDING.name())) {
				transaction.setStatus(transactionStatus.getStatus().toUpperCase());
				investment.setStatus(InvestmentStatusEnum.CANCELLED.name());
				dealService.updateDealStatus(false, investment.getDealid());
				transactionRepository.save(transaction);
			}
		}
	}

	public boolean verifyXVerifyHeader(String xVerify, String responseBody) {
		logger.info("Verification of transaction details started");
		logger.info("response body :: " + responseBody);
		logger.info("xVerify ::" + xVerify);
		String merchantId = "M22LC1E47PCGZ";
		String saltKey = "515f5b48-4292-445a-963b-759afeb2855f";
		Integer saltIndex = 1;
		PhonePePaymentClient phonePeClient = new PhonePePaymentClient(merchantId, saltKey, saltIndex, Env.PROD, true);
		logger.info("verification of transaction details completed");
		return phonePeClient.verifyResponse(xVerify, responseBody);
	}

	@Async
	private void sendTransMsgToSeller(int dealid) {
		DealDetailInfo dealDetailInfo = dealRepository.getDealDetailedInfo(dealid);
		messageService.sendInvDetailToSeller(dealDetailInfo);
	}
}
