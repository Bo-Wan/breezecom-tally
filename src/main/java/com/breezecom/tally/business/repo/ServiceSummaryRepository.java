package com.breezecom.tally.business.repo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.breezecom.tally.model.ServiceSummary;

@Repository
public interface ServiceSummaryRepository extends CrudRepository<ServiceSummary, Long> {
	// MessageRecord findByName(String name);
	
	List<ServiceSummary> findByServiceIdAndYearAndMonth(String serviceId, int year, int month);
	
	Integer getTotalAdjustmentByServiceId(@Param("clientId") String clientId, @Param("year") int year, @Param("month") int month);

}
