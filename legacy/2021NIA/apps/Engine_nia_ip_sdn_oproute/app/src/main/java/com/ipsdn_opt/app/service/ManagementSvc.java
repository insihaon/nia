package com.ipsdn_opt.app.service;

import java.util.Map;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ipsdn_opt.app.component.ssh.ShellManager;
import com.ipsdn_opt.app.model.LoginUser;
import com.ipsdn_opt.app.model.Response;
import com.ipsdn_opt.app.model.User;
import com.ipsdn_opt.app.repository.CommandServerRepository;
import com.ipsdn_opt.app.repository.NodeRepository;
import com.ipsdn_opt.app.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ManagementSvc {
    @Autowired
    UserRepository userRepository;
    @Autowired
    CommandServerRepository commandServerRepository;
    @Autowired
    NodeRepository nodeRepository;
    @Autowired
    ShellManager sm;
    @Autowired
    private final PasswordEncoder passwordEncoder;
    @Autowired
    EntityManager em;

    private Response response = new Response(true, "", null);
    
    @Transactional(readOnly = true)
    public Response signUpUser(User user) {
        try {
            String password = user.getPassword();
            if(!password.equals(user.getPasswordConfirm())) {
                response.setStatus(false);
                response.setMessage("input password different.");
                response.setData(null);
                return response;
            }
            user.setPassword(passwordEncoder.encode(password));
            userRepository.save(user);
            response.setStatus(true);
            response.setMessage("User Registration Success");
            response.setData(null);
        }
        catch(Exception e) {
            response.setStatus(false);
            response.setMessage("User Registration Fail. -> " + e.getMessage());
            response.setData(e.getCause());
        }
        return response;
    }
    public Response signInUser(LoginUser inputUser) {
        try {
            User dbUser = userRepository.findByLoginid(inputUser.getLoginid());
            
            if(dbUser==null) return new Response(false, "User is not exist.", null);
            else {
                if(!passwordEncoder.matches(inputUser.getPassword(), dbUser.getPassword())) return new Response(false, "Password not correct.", null);
            }
        }
        catch(Exception e) {
            return new Response(false, "Login Fail. -> " + e.getMessage(), e.getCause());
        }

        return new Response(true, "Login Success.", null);
    }
    public Response dockerDistributeProbe() {
        boolean isSuccess = true;
        String result = null;
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");
            headers.add("Authorization", "Basic YWRtaW46bGFkeXJrcmsh"); //Lady가가&
            HttpEntity<String> entity = new HttpEntity<>(headers);

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Map> responseEntity = restTemplate.exchange("http://awx.koren.kr:8010/api/v2/job_templates/50/launch/", HttpMethod.POST, entity, Map.class);
            
            ObjectMapper mapper = new ObjectMapper();
            isSuccess = responseEntity.getStatusCode().is2xxSuccessful();
            result = mapper.writeValueAsString(responseEntity.getBody());
        }
        catch(Exception e) {
            return new Response(false, "Probe(Docker) Distribution Error -> " + e.getMessage(), null);
        }
        return new Response(isSuccess, "Probe(Docker) Distribution Complete. (available about 1 minute later)", null);
    }

}
