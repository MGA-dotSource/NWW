/**
 * 
 */
package org.nww.modules.search;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.nww.app.AbstractApplicationController;
import org.nww.modules.pinboard.orm.PinboardEntry;
import org.nww.modules.pinboard.orm.PinboardManager;
import org.nww.modules.search.orm.pinboard.ES_PinboardEntry;
import org.nww.modules.search.orm.pinboard.ES_PinboardEntryRepository;
import org.nww.modules.search.orm.suppliers.ES_Supplier;
import org.nww.modules.search.orm.suppliers.ES_SupplierRepository;
import org.nww.modules.search.orm.users.ES_User;
import org.nww.modules.search.orm.users.ES_UserRepository;
import org.nww.modules.suppliers.orm.Supplier;
import org.nww.modules.suppliers.orm.SupplierManager;
import org.nww.modules.users.orm.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * The search controller handles all search requests.
 * @author mga
 *
 */
@Controller
@RequestMapping(value = "/network/search")
public class SearchController extends AbstractApplicationController {
	
	@Autowired
	private ES_UserRepository userRepo;
	
	@Autowired
	private ES_PinboardEntryRepository pinboardEntryRepo;
	
	@Autowired
	private ES_SupplierRepository supplierRepo;
	
	@Resource(name = "PinboardManager")
	private PinboardManager pinboardMgr;
	
	@Resource(name = "SupplierManager")
	private SupplierManager supplierMgr;
	
	@RequestMapping(value = "", params = "q", method = RequestMethod.GET)
	public String searchAll(@RequestParam(required = true, value = "q") String query, Model model) {
		StopWatch sw = new StopWatch();
		sw.start();
		
		// make sure search term is not longer then 100 chars
		if(query.length() > 100) {
			query = query.substring(0, 100);
		}
		
		Pageable p = new PageRequest(0, 256);
		
		Page<? extends User> users = mapUserResult(userRepo.findUsers(query, p), p);
		Page<? extends PinboardEntry> pinboardEntries = mapPinboardEntriesResult(pinboardEntryRepo.findPinboardEntries(query, p), p);
		Page<? extends Supplier> suppliers = mapSupplierResult(supplierRepo.findSuppliers(query, p), p);
		
		model.addAttribute("Query", query);
		model.addAttribute("TotalResultCount", users.getTotalElements() + pinboardEntries.getTotalElements() + suppliers.getTotalElements());
		model.addAttribute("Users", users);
		model.addAttribute("PinboardEntries", pinboardEntries);
		model.addAttribute("Suppliers", suppliers);
		
		sw.stop();
		
		model.addAttribute("TotalTime", sw.getTotalTimeSeconds());
		
		return "search/overview";
	}
	
	private Page<User> mapUserResult(Page<ES_User> users, Pageable p) {
		List<User> mappedUsers = new ArrayList<>();
		
		for (ES_User u : users) {
			mappedUsers.add(getUserManager().findOne(u.getUUID()));
		}
		
		return new PageImpl<>(mappedUsers, p, users.getTotalElements());
	}
	
	private Page<PinboardEntry> mapPinboardEntriesResult(Page<ES_PinboardEntry> entries, Pageable p) {
		List<PinboardEntry> mappedEntries = new ArrayList<>();
		
		for (ES_PinboardEntry e : entries) {
			mappedEntries.add(pinboardMgr.findOne(e.getUUID()));
		}
		
		return new PageImpl<>(mappedEntries, p, entries.getTotalElements());
	}
	
	private Page<? extends Supplier> mapSupplierResult(Page<ES_Supplier> suppliers, Pageable p) {
		List<Supplier> mappedSuppliers = new ArrayList<>();
		
		for (ES_Supplier s : suppliers) {
			mappedSuppliers.add(supplierMgr.findOne(s.getUUID()));
		}
		
		return new PageImpl<>(mappedSuppliers, p, suppliers.getTotalElements());
	}
}
