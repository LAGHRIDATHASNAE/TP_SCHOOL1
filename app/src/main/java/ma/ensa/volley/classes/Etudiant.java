package ma.ensa.volley.classes;

public class Etudiant extends User{

    private String FirstName;
    private String LastName;
    private String telephone;



    public Etudiant(String FirstName, String LastName, String telephone){
        super();
        this.FirstName= FirstName;
        this.LastName=LastName;
        this.telephone=telephone;
    }
    public String getFirstName() {
        return FirstName;
    }
    public void setFirstName(String firstName) {
        this.FirstName = firstName;
    }
    public String getLastName() {
        return LastName;
    }
    public void setLastName(String LastName) {
        this.LastName = LastName;
    }
    public String getTelephone() {
        return telephone;
    }
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

}
