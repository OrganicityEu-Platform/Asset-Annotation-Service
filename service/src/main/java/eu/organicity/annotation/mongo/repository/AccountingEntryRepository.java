package eu.organicity.annotation.mongo.repository;


import eu.organicity.annotation.domain.accounting.AccountingEntry;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AccountingEntryRepository extends MongoRepository<AccountingEntry, String> {

    AccountingEntry findById(String id);

    List<AccountingEntry> findByService(String service);

    List<AccountingEntry> findBySub(String sub);

    List<AccountingEntry> findByAction(String action);

}