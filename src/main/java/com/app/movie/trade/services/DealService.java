package com.app.movie.trade.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.app.movie.trade.entities.Deal;
import com.app.movie.trade.entities.DealCountRequest;
import com.app.movie.trade.entities.DealRequestObj;
import com.app.movie.trade.entities.DealResult;
import com.app.movie.trade.entities.Movie;
import com.app.movie.trade.entities.MovieDealCount;
import com.app.movie.trade.entities.MovieDealCountPojo;
import com.app.movie.trade.entities.Theatre;
import com.app.movie.trade.entities.TheatreDeal;
import com.app.movie.trade.entities.TheatreDealRequest;
import com.app.movie.trade.repositories.DealRepository;
import com.app.movie.trade.repositories.MovieRepository;
import com.app.movie.trade.repositories.TheatreRepository;

@Service
public class DealService {
	@Autowired
	DealRepository dealRepository;
	@Autowired
	MovieRepository movieRepository;
	@Autowired
	TheatreRepository theatreRepository;
	@Autowired
	RedisTemplate<Object, Object> redisTemplate;
	Logger logger = LoggerFactory.getLogger(DealService.class);
	private static final Integer EXPIRE_MIN = 5;

	public Deal createDeal(Deal deal) {
		logger.info("Creation of deal started ::"+deal.toString());
		Theatre theatre = theatreRepository.getReferenceById(deal.getTheatre_id());
		deal.setCity_id(theatre.getCity_id());
		deal.setCreated_date(LocalDate.now());
		deal.setUpdated_date(LocalDate.now());
		Deal createdDeal = dealRepository.save(deal);
		logger.info("deal created successfully");
		return createdDeal;
	}

	public Page<DealResult> getDealsByMovie(DealRequestObj dealRequestObj, Pageable pageable) {
		logger.info("fetching deals by movie with request parameters :: " + " City :: " + dealRequestObj.getCity_id()
				+ " date " + dealRequestObj.getDate() + " movieid " + dealRequestObj.getMovieid());
		logger.info("pageable parameters :: getDealsByMovie " + " page Number " + pageable.getPageNumber()
				+ " pagination size " + pageable.getPageSize());
		List<DealResult> dealResults = new ArrayList<>();
		Page<DealResult> resultOutput = null;
		if (pageable.getPageNumber() == 0) {
			List<Deal> deals = dealRepository.getDealsByMovieId(dealRequestObj.getMovieid(), dealRequestObj.getDate(),
					dealRequestObj.getCity_id());

			if (!deals.isEmpty()) {
				Map<Integer, List<Deal>> dealsByTheatre = deals.stream()
						.collect(Collectors.groupingBy(Deal::getTheatre_id));

				dealsByTheatre.forEach((key, value) -> {
					DealResult dealResult = new DealResult();
					dealResult.setDeals(value);
					dealResult.setCount(value.size());
					Theatre theatre = theatreRepository.getReferenceById(key);
					dealResult.setPropertyName(theatre.getProperty_name());
					dealResult.setTheatreName(theatre.getTheatre_name());
					dealResult.setTheatreCapacity(theatre.getCapacity());
					dealResults.add(dealResult);
				});
				logger.info("count of deals grouped by movieid :: getDealsByMovie " + dealResults.size());
				resultOutput = pagingDealsByMovie(dealResults, pageable.getPageSize(), pageable.getPageNumber());
			} else {
				logger.info("no deals found with given details :: "+dealRequestObj);
			}
			redisTemplate.opsForValue().set(dealRequestObj, dealResults);
			redisTemplate.expire(dealRequestObj, EXPIRE_MIN, TimeUnit.MINUTES);
		} else {
			@SuppressWarnings("unchecked")
			List<DealResult> cacheDeals = (List<DealResult>) redisTemplate.opsForValue().get(dealRequestObj);
			if (cacheDeals != null) {
				resultOutput = pagingDealsByMovie(cacheDeals, pageable.getPageSize(), pageable.getPageNumber());
			}
		}

		return resultOutput;
	}

	private Page<DealResult> pagingDealsByMovie(List<DealResult> list, int pagesize, int pageNo) {

		int totalpages = list.size() / pagesize;
		PageRequest pageable = PageRequest.of(pageNo, pagesize);

		int max = pageNo >= totalpages ? list.size() : pagesize * (pageNo + 1);
		int min = pageNo > totalpages ? max : pagesize * pageNo;

		Page<DealResult> pageResponse = new PageImpl<DealResult>(list.subList(min, max), pageable, list.size());
		return pageResponse;
	}

	private Page<MovieDealCountPojo> toPage(List<MovieDealCountPojo> list, int pagesize, int pageNo) {

		int totalpages = list.size() / pagesize;
		PageRequest pageable = PageRequest.of(pageNo, pagesize);

		int max = pageNo >= totalpages ? list.size() : pagesize * (pageNo + 1);
		int min = pageNo > totalpages ? max : pagesize * pageNo;

		Page<MovieDealCountPojo> pageResponse = new PageImpl<MovieDealCountPojo>(list.subList(min, max), pageable,
				list.size());
		return pageResponse;
	}

	public Page<MovieDealCountPojo> getMovieDealCountByCity(DealCountRequest dealCountRequest, Pageable pageable) {
		logger.info("Fetching number of deals by date and city,langauge, movie name based on request parameters  :: "
				+ dealCountRequest.getCity_id() + "," + dealCountRequest.getName() + ","
				+ dealCountRequest.getLanguage());
		Page<MovieDealCountPojo> pageList = null;
		if (pageable.getPageNumber() == 0) {
			List<MovieDealCount> dealCountByMovieList = dealRepository.getMovieDealCountByCity(
					dealCountRequest.getCity_id(), dealCountRequest.getName(), dealCountRequest.getLanguage());
			List<MovieDealCountPojo> countPojos = convertMovieDealCountToPojo(dealCountByMovieList);
			pageList = toPage(countPojos, pageable.getPageSize(), pageable.getPageNumber());
			redisTemplate.opsForValue().set(dealCountRequest, countPojos);
			redisTemplate.expire(dealCountRequest, EXPIRE_MIN, TimeUnit.MINUTES);
			logger.info("Total groups by movie :: " + pageList.getTotalElements());
		} else {
			@SuppressWarnings("unchecked")
			List<MovieDealCountPojo> cacheDealCountByMovies = (List<MovieDealCountPojo>) redisTemplate.opsForValue()
					.get(dealCountRequest);
			if (cacheDealCountByMovies != null) {
				pageList = toPage(cacheDealCountByMovies, pageable.getPageSize(), pageable.getPageNumber());
			}
		}
		logger.info("Request to fetch number of deals by date and city,langauge, movie name");
		return pageList;
	}

	public List<LocalDate> getDealDatesByMovie(int movieid) {
		logger.info("Fetching deal dates for a  movie with id :: "+movieid);
		return dealRepository.getDealDatesByMovie(movieid);
	}

	public List<MovieDealCountPojo> convertMovieDealCountToPojo(List<MovieDealCount> dealCountByMovieList) {
		List<MovieDealCountPojo> countPojos = new ArrayList<>();
		MovieDealCountPojo countPojo = null;
		for (MovieDealCount movieDealCount : dealCountByMovieList) {
			countPojo = new MovieDealCountPojo();
			countPojo.setCbfcrating(movieDealCount.getCbfcrating());
			countPojo.setCount(movieDealCount.getCount());
			countPojo.setReleasedate(movieDealCount.getReleasedate());
			countPojo.setDuration(movieDealCount.getDuration());
			countPojo.setFormattype(movieDealCount.getFormattype());
			countPojo.setGenre(movieDealCount.getGenre());
			countPojo.setImageurl(movieDealCount.getImageurl());
			countPojo.setLanguage(movieDealCount.getLanguage());
			countPojo.setMovieid(movieDealCount.getMovieid());
			countPojo.setName(movieDealCount.getName());
			countPojos.add(countPojo);
		}
		return countPojos;
	}

	public void updateDealStatus(boolean isActive, int dealid) {
		logger.info("Updating deal status with id :: "+dealid);
		Deal deal = dealRepository.getReferenceById(dealid);
		deal.setIs_invested(isActive);
		deal.setUpdated_date(LocalDate.now());
		dealRepository.save(deal);
		logger.info("deal status updated successfully");
	}

	public Page<TheatreDeal> getDealsByTheatre(TheatreDealRequest dealRequest, Pageable pageable) {
		logger.info("Fetching deals by theatre for a given date :: "+dealRequest.toString());
		Page<TheatreDeal> pageTheatreDeals = null;
		if (pageable.getPageNumber() == 0) {

			List<Deal> deals = dealRepository.getDealsByTheatreAndDate(dealRequest.getTheatre_id(),
					dealRequest.getDate());
			Map<Integer, List<Deal>> dealsByMovie = deals.stream().collect(Collectors.groupingBy(Deal::getMovieid));
			List<TheatreDeal> theatreDeals = convertDealsByMovieMapToTheatreDeals(dealsByMovie);
			pageTheatreDeals = (Page<TheatreDeal>) pagingDealsByTheatre(theatreDeals, pageable.getPageSize(),
					pageable.getPageNumber());
			redisTemplate.opsForValue().set(dealRequest, theatreDeals);
			redisTemplate.expire(dealRequest, EXPIRE_MIN, TimeUnit.MINUTES);
		} else {
			@SuppressWarnings("unchecked")
			List<TheatreDeal> cachedTheatreDeals = (List<TheatreDeal>) redisTemplate.opsForValue().get(dealRequest);
			pageTheatreDeals = (Page<TheatreDeal>) pagingDealsByTheatre(cachedTheatreDeals, pageable.getPageSize(),
					pageable.getPageNumber());
		}
		logger.info("completed fetching deals by theatre with page and size ::"+pageable.toString());
		return pageTheatreDeals;
	}

	private List<TheatreDeal> convertDealsByMovieMapToTheatreDeals(Map<Integer, List<Deal>> dealsByMovie) {
		List<TheatreDeal> theatreDeals = new ArrayList<>();
		dealsByMovie.forEach((key, value) -> {
			TheatreDeal theatreDeal = new TheatreDeal();
			Movie movie = movieRepository.getReferenceById(key);
			theatreDeal.setDeals(value);
			theatreDeal.setMovie(movie);
			theatreDeals.add(theatreDeal);
		});
		return theatreDeals;
	}

	private Page<TheatreDeal> pagingDealsByTheatre(List<TheatreDeal> theatreDeals, int pagesize, int pageNo) {

		int totalpages = theatreDeals.size() / pagesize;
		PageRequest pageable = PageRequest.of(pageNo, pagesize);

		int max = pageNo >= totalpages ? theatreDeals.size() : pagesize * (pageNo + 1);
		int min = pageNo > totalpages ? max : pagesize * pageNo;

		Page<TheatreDeal> pageResponse = new PageImpl<TheatreDeal>(theatreDeals.subList(min, max), pageable,
				theatreDeals.size());
		return pageResponse;
	}
	
	public List<LocalDate> getDealDatesByTheatre(int theatre_id) {
		logger.info("fetching deal dates of a theatre with id: "+theatre_id);
		return dealRepository.getDealDatesByTheatre(theatre_id);
	}
	
	public void updateDealPrice(int dealid, double dealPrice) {
		logger.info("Started updating total deal price for a deal with id :: "+dealid);
		Deal deal = dealRepository.getReferenceById(dealid);
		deal.setUpdated_date(LocalDate.now());
		deal.setTotal_dealprice(dealPrice);
		deal.setMaxprofit(dealPrice);
		dealRepository.save(deal);
		logger.info("Successfully updated total deal price for a deal with id :: "+dealid);
	}
}
