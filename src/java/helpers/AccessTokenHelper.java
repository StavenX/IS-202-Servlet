/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helpers;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author tobia
 */
public class AccessTokenHelper {
    private final String[] roles;
    private HttpServletRequest request;
    private String username;
    private String userRole;
    
    
    public AccessTokenHelper(HttpServletRequest request) {
        this.request = request;
        String[] r = {"Student", "Lecturer", "Admin"};
        this.roles = r;
    }
    
    public String getUsername() {
        String token = getToken();
        String toBeReplaced = token.substring(0, token.indexOf("[") + 1);
        token = token.replace(toBeReplaced, "");
        
        toBeReplaced = token.substring(token.indexOf("("));
        
        for (String role: roles) {
            if (toBeReplaced.contains(role))
                userRole = role;
        }
        username = token.replace(toBeReplaced, "");
        
        return username;
    }
    
    public String getUserRole() {
        getUsername();
        return userRole;
    }
    
    public String getToken() {
        return request.getUserPrincipal().toString();
    }
    

    public String[] getRoles() {
        return roles;
    }
    
    
}
