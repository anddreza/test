package qaops.automation.api.teste;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.BeforeClass;
import org.junit.Test;
import qaops.automation.api.dominio.Usuario;

import static io.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.is;

public class RegistroTeste extends BaseTeste{

    private static final String REGISTRA_USUARIO_ENDPOINT = "/register";

    @Test
    public void testNaoEfetuaRegistroQuandoSenhaEstaFaltando(){
        Usuario usuario = new Usuario();
        usuario.setEmail("sydeny@fife");

        given().
               // contentType(ContentType.JSON).
                body(usuario).
        when().
                post(REGISTRA_USUARIO_ENDPOINT).
        then().
            statusCode(HttpStatus.SC_BAD_REQUEST).
            body("error", is("Missing password"));
    }

}
