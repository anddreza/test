package qaops.automation.api.teste;

import io.restassured.RestAssured;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import qaops.automation.api.dominio.Usuario;

import static io.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;

public class TesteRegistro extends TesteBase {

    private static final String REGISTRA_USUARIO_ENDPOINT = "/register";
    private static final String LOGIN_USUARIO_ENDPOINT = "/login";


    @BeforeClass
    public static void setupRegistro(){
        RestAssured.responseSpecification = new ResponseSpecBuilder()
               // .expectContentType(ContentType.JSON)
                .expectStatusCode(HttpStatus.SC_BAD_REQUEST)
                .build();
    }


    @Test
    public void testeNaoEfetuaRegistroQuandoSenhaEstaFaltando(){
        Usuario usuario = new Usuario();
        usuario.setEmail("sydeny@fife");

        given().
               // contentType(ContentType.JSON).
                body(usuario).
        when().
                post(REGISTRA_USUARIO_ENDPOINT).
        then().
        //    statusCode(HttpStatus.SC_BAD_REQUEST).
            body("error", is("Missing password"));
    }


    //Esse teste deveria estar no TesteLogin, porém está aqui para mostrar
    //a funcionalidade de múltiplos setups e ResponseSpec
    @Test
    public void testeLoginNaoEfetuadoQuandoSenhaEstaFaltando(){
        Usuario usuario = new Usuario();
        usuario.setEmail("sydeny@fife");

        given().
                body(usuario).
        when().
                post(LOGIN_USUARIO_ENDPOINT).
        then().
                body("error", is("Missing password"));
    }

}
