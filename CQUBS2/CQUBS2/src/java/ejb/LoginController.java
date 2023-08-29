package ejb;

import jakarta.ejb.EJB;
import jakarta.inject.Named;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.FacesContext;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.MessageDigest;

/**
 *
 * @author Amy
 */

@Named(value = "loginController")
@SessionScoped
public class LoginController implements Serializable
{
    @EJB
    private UsersEJB usersEJB;
    
    @EJB
    private LoginEJB loginEJB;
    
    private final String PAGE_NAME = "Volunteer Login";
    
    private String username,password = "";
    
    public LoginController() {
        
    }
    
    public String login() throws NoSuchAlgorithmException {
        String navResult = "";
        FacesContext ctx = FacesContext.getCurrentInstance();
        
        //Does the user with the email address exist?
        Login userAccount = loginEJB.findLoginByEmail(username);
        if(userAccount == null)
            //No user found, wrong login 
            navResult = "login.faces";
        else {
            //Import salt data from DB
            MessageDigest digest = MessageDigest.getInstance("SHA-512");
            String passwordSalt = userAccount.getSalt();
            byte[] byteSalt = new byte[passwordSalt.length()/2];
            for(int i=0; i<byteSalt.length; i++) {
                int index = i*2;
                int j = Integer.parseInt(passwordSalt.substring(index, index + 2), 16);
                byteSalt[i] = (byte) j;
            }
            
            //Convert input String to byte array and encrypt with salt
            byte[] pwDigest = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for(int i=0; i<pwDigest.length; i++) {
                sb.append(Integer.toString((pwDigest[i] & 0xff) + 0x100, 16).substring(1));
            }
            String passwordHash = sb.toString();
            
            //Check if password hash matches
            if(passwordHash.equals(userAccount.getPassword())) {   
                Users user = usersEJB.findVolByEmail(username);
                username = "";
                password = "";
                    
                ctx.getExternalContext().getSessionMap().put("user", user);
                
                navResult = "index.faces";
            }
            else {
                username = "";
                password = "";
                navResult = "login.faces";
            }
        }
        
        return navResult;
    }

    public String getUsername() 
    {
        return username;
    }

    public void setUsername(String username) 
    {
        this.username = username;
    }

    public String getPassword() 
    {
        return password;
    }

    public void setPassword(String password) 
    {
        this.password = password;
    }

    public String getPAGE_NAME() 
    {
        return PAGE_NAME;
    }
}