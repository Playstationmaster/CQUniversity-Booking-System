/*
 * Service.java - Service entity class
 * Stores details of Services offered
 */
package ejb;

import java.io.Serializable;
import java.util.ArrayList;
import jakarta.persistence.*;

/**
 * * @author HeimannK
 */
@Entity
public class Service implements Serializable {

   // Attributes
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="SERV_ID")
    private Long serviceId;
    @Column(name="SERV_NAME")
    private String serviceName;
    @Column(name="SERV_DESC")
    private String serviceDescription;
    @Column(name="SERV_PRICE")
    private Double servicePrice;
    @Column(name="IMAGE_URL")
    private String imageUrl;
    @ManyToOne
    @JoinColumn(name="CAT_ID")
    private Category category;
    @OneToMany(mappedBy = "service")
    private ArrayList<ServiceAtLocation> salList;
    
    // Constructors
    public Service() {
        
    }
    
    public Service(String serviceName, String serviceDescription, Double servicePrice, String imageUrl, Category category) {
        this.serviceName = serviceName;
        this.serviceDescription = serviceDescription;
        this.servicePrice = servicePrice;
        this.imageUrl = imageUrl;
        this.category = category;
    }
    
    public Service(String serviceName, String serviceDescription, Double servicePrice, String imageUrl, Category category, ArrayList<ServiceAtLocation> salList) {
        this.serviceName = serviceName;
        this.serviceDescription = serviceDescription;
        this.servicePrice = servicePrice;
        this.imageUrl = imageUrl;
        this.category = category;
        this.salList = salList;
    }
    
    // Getters & Setters
    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }
    
    public String getServiceName() {
        return serviceName;
    }
    
    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
    
    public String getServiceDescription() {
        return serviceDescription;
    }
    
    public void setServiceDescription(String serviceDescription) {
        this.serviceDescription = serviceDescription;
    }
    
    public Double getServicePrice(){
        return servicePrice;
    }
    
    public void setServicePrice(Double servicePrice) {
        this.servicePrice = servicePrice;
    }
    
    public String getImageUrl() {
        return imageUrl;
    }
    
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    
   public Category getCategory() {
       return category;
   }
   
   public void setCategory(Category category) {
       this.category = category;
   }
    
    public ArrayList<ServiceAtLocation> getSalList() {
        return salList;
    }
    
    public void setSalList(ArrayList<ServiceAtLocation> salList) {
        this.salList = salList;
    }
}
