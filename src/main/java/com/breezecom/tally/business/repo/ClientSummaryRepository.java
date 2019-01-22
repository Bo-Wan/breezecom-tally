package com.breezecom.tally.business.repo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.breezecom.tally.model.ClientSummary;

@Repository
public interface ClientSummaryRepository extends CrudRepository<ClientSummary, Long> {
	// MessageRecord findByName(String name);

	List<ClientSummary> findByClientIdAndYearAndMonth(String clientId, int year, int month);
}
