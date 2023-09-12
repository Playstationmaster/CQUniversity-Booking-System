package ejb;

import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import java.io.IOException;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.List;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;

/**
 * This is a class for sending email notifications to volunteers who subscribed to events. 
 * List of email address is obtained from ServiceAtLocation Registration list.
 * Jakarta Mail API is used for SMTP.
 */

@Named(value = "notifController")
@SessionScoped
public class NotifController implements Serializable {
    @EJB
    private ServiceAtLocationEJB salEJB;
    @EJB
    private RegistrationEJB regEJB;
    
    private Long salId = 0L;
    
    private ServiceAtLocation sal = new ServiceAtLocation();
    private String firstName, lastName, phone, detailsText;
    private final String PAGE_NAME = "Send Email Notification";
    private static final DecimalFormat df = new DecimalFormat("0.00");
    
    public NotifController()  {
        
    }
    
    public void init() {
        FacesContext ctx = FacesContext.getCurrentInstance();
        
        firstName = "";
        lastName = "";
        phone = "";
        detailsText = "";
        
        if(salId > 0L)
            sal = salEJB.findSALById(salId);
        else {
            try {
                ctx.getExternalContext().redirect("service_details_staff.faces");
            }
            catch(IOException e) {
                
            }
        }
    }
    
    public void sendEmail() {
        try {
            //Get email list
            List<Registration> registrationList = sal.getRegList(); //Registration list of the SAL
            List<String> emailList = null; //List of the email address of volunteers will be saved here
            
            String emailReceivers = "";
            if(!registrationList.isEmpty()) {
                //Extract email list from SAL registrationList
                for(int i=0; i< registrationList.size(); i++) {
                    //This one currently collects all addresses. non-subscriber detections might be implemented later. 
                    emailList.add(registrationList.get(i).getVolunteer().getEmail());
                }
                
                //parse emailList values to one String for SMTP sender list preparation
                for(int i=0; i< (registrationList.size()-1); i++) {
                    emailReceivers += emailList.get(i);
                    emailReceivers += ", ";
                }
                emailReceivers += emailList.get(emailList.size()-1);
            }
            
            //Send email via SMTP TLS
            final String smtpService = ""; //Enter your SMTP hostname
            final String smtpPort = ""; //Enter your SMTP port
            final String senderAddress = ""; //email address of sender
            final String senderPassword = ""; //email password of sender
            
            final String emailSubject = "Event reminder email: " + sal.getService().getServiceName() + " at " + sal.getLocation().getLocationName();
            final String emailContent = "Dear volunteers, \n"
                    + "You have received this email as you registered for the event \"" + sal.getService().getServiceName() + "\", "
                    + "at your selected location, " + sal.getLocation().getLocationName() + ". "
                    + "Please visit the website to check your event details again. \n\n"
                    + "Kind Regards, \n" + firstName + " " + lastName;
            
            Properties propSMTP = new Properties();
            propSMTP.put("mail.smtp.host",smtpService);
            propSMTP.put("mail.smtp.port", smtpPort);
            propSMTP.put("mail.smtp.auth", "true");
            propSMTP.put("mail.smtp.starttls.enable","true");
            
            Session sessionSMTP = Session.getInstance(propSMTP, new jakarta.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(senderAddress, senderPassword);
                }
            });
            
            Message message = new MimeMessage(sessionSMTP);
            message.setFrom(new InternetAddress(senderAddress));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailReceivers));
            message.setSubject(emailSubject);
            message.setText(emailContent);
            Transport.send(message);
        }
        //issues with sending emails
        catch(Exception e) {
            
        }
    }
    
    public String renderPrice(double price) {
        String priceAsString = "";
        if(price > 0.0)
            priceAsString = "$" + df.format(price);
        else
            priceAsString = "FREE";
        
        return priceAsString;
    }
    
    public List<Registration> getRegList(ServiceAtLocation servAtLocation) {
        List<Registration> regList = regEJB.findRegistrationsBySAL(servAtLocation);
        return regList;
    }

    public Long getSalId() {
        return salId;
    }

    public void setSalId(Long salId) {
        this.salId = salId;
    }

    public ServiceAtLocation getSal() {
        return sal;
    }

    public void setSal(ServiceAtLocation sal) {
        this.sal = sal;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDetailsText() {
        return detailsText;
    }

    public void setDetailsText(String detailsText) {
        this.detailsText = detailsText;
    }

    public String getPAGE_NAME() {
        return PAGE_NAME;
    }
}