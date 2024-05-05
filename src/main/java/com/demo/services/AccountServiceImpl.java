	package com.demo.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.demo.Repository.AccountRepository;

import com.demo.entities.Role;
import com.demo.entities.Account;

@Service("accountService")
public class AccountServiceImpl implements AccountService{

	@Autowired
	private AccountRepository accountRepository;
		
	@Override
	public boolean login(String username, String password) {
		try {
			return accountRepository.login(username, password);
		} catch (Exception e) {
			return false;
		}
	}

	
	@Override
	public boolean save(Account account) {
		try {
			accountRepository.save(account);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	    Account account = accountRepository.findByUsername(username);
	    if (account == null) {
	        throw new UsernameNotFoundException("Username not found");
	    } else if (!account.isStatus()) {
	       // throw new DisabledException("Your account is disabled. Please contact the administrator.");
	        throw new DisabledException("If you can't login, please contact superadmin. ");
	    } else {
	        List<GrantedAuthority> authorities = new ArrayList<>();
	        authorities.add(new SimpleGrantedAuthority(account.getRole().getName()));
	        return new User(username, account.getPassword(), authorities);
	    }
	}
	
	
	@Override
	public Account findByUsername(String username) {
		try {
			return accountRepository.findByUsername(username);
		} catch (Exception e) {
			return null;
		}
	}


	@Override
	public boolean check(String username) {
		
		return username.equals("abc");
	}


	@Override
	public Iterable<Account> findAll() {
		return accountRepository.findAll();
	}


	@Override
	public Account find(int id) {
		return accountRepository.findById(id).get();
	}


	@Override
	public boolean delete(int id) {
		try {
			accountRepository.delete(accountRepository.findById(id).get());
			return true;			
		}catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}


	@Override
	public boolean editStatus(int id, String status) {
		
		try {
			Account account1 = accountRepository.findById(id).get();
			//account1.setStatus(status);
			accountRepository.save(account1);
		}catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}


	@Override
	public Account findByEmail(String email) {
		try {
			return accountRepository.findByEmail(email);
		} catch (Exception e) {
			return null;
		}
	}


	@Override
	public List<Account> limit() {
		return accountRepository.limit();
	}


	@Override
	public List<Account> limit12() {
		return accountRepository.limit12();
	}
	
	@Override
    public Integer getCurrentUserId() {
        // Lấy thông tin Authentication hiện tại từ SecurityContextHolder
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        // Kiểm tra xem người dùng đã đăng nhập chưa
        if (authentication != null && authentication.isAuthenticated()) {
            // Lấy tên người dùng đã đăng nhập
            String username = authentication.getName();
            
            // Tìm kiếm tài khoản tương ứng trong cơ sở dữ liệu
            Account account = accountRepository.findByUsername(username);
            
            // Kiểm tra xem tài khoản có tồn tại không
            if (account != null) {
                // Trả về id của người dùng
                return account.getId();
            }
        }
        
        // Trả về null nếu không tìm thấy hoặc không có người dùng nào đăng nhập
        return null;
    }


	@Override
	public List<Account> findByKeyword(String keyword) {
		return accountRepository.findByKeyword(keyword);
	}


	@Override
	public String getHashedPassword(String username) {
		return accountRepository.findByUsername(username).getPassword();
	}


	@Override
	public List<Account> searchByKeyword(String keyword) {
		return accountRepository.searchByKeyword(keyword);
	}


	
}
