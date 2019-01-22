package com.breezecom.tally.business.repo;

import java.util.Date;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.breezecom.tally.model.MessageRecord;

@Repository
public interface MessageRecordRepository extends CrudRepository<MessageRecord, Integer> {
	
//	List<MessageRecord> findByDateBetween(Date start, Date end);

    List<String> listActiveServiceIds(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
    List<String> listActiveClientIds(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
    
    int shortCountByServiceId(@Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("serviceId") String serviceId);
    int shortCountByClientId(@Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("client_id") String client_id);

    int longCountByServiceId(@Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("serviceId") String serviceId);
    int longCountByClientId(@Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("client_id") String client_id);
    
    int totalCountByServiceId(@Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("serviceId") String serviceId);
    int totalCountByClientId(@Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("client_id") String client_id);
    
    
    List<String> getDidByServiceId(@Param("serviceId") String serviceId, @Param("startDate") Date startDate, @Param("endDate") Date endDate);
    List<String> getClientIdByServiceId(@Param("serviceId") String serviceId, @Param("startDate") Date startDate, @Param("endDate") Date endDate);
}
