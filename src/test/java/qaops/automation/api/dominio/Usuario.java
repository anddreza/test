package qaops.automation.api.dominio;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.*;

@JsonIgnoreProperties(ignoreUnknown = true)
//@Getter
//@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Usuario {

    @JsonAlias("first_name")
    private String name;
    private String job;
    private String email;
    @JsonAlias("last_name")
    private String lastName;

    //public Usuario(){}
//    public Usuario(String name, String job, String email) {
//        this.name = name;
//        this.job = job;
//        this.email = email;
//    }

   // public String getName() {
   //     return name;
   // }

//    public String getJob() {
//        return job;
//    }

    //ou coloca como public ou teria que inserir o get
 //   public String getEmail(){
   //     return email;
   // }

   // public void setEmail(String email){
     //   this.email = email;
   // }

    //serialização - transformando um objeto em um json
 //   @JsonGetter("ultimo_nome")
 //   public String getLastName() {
  //      return lastName;
  //  }

    //deserealização - transformando um json em um objeto
    @JsonSetter("sobrenome")
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
