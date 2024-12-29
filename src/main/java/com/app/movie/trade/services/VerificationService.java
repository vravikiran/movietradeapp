package com.app.movie.trade.services;

import org.springframework.stereotype.Service;

import com.app.movie.trade.entities.BankAccountDetails;
import com.app.movie.trade.entities.KycDetails;

@Service
public class VerificationService {
	
	public boolean verifyBankDetails(BankAccountDetails bankAccountDetails) {
		return true;
	}
	
	public boolean verifyKycDetails(KycDetails kycDetails) {
		return true;
	}
	
	public boolean verifyPanno(String panno) {
		return true;
	}
}
