package qaops.automation.api.teste;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.BeforeClass;
import org.junit.Test;
import qaops.automation.api.dominio.Usuario;

import static io.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.*;


public class UsuarioTeste extends BaseTeste{

    private static final String LISTA_USUARIOS_ENDPOINT = "/users";
    private static final String CRIAR_USUARIO_ENDPOINT = "/user";

    @Test
    public void testListaMetadosDoUsuario(){
        given().
                params("page", "2").
        when().
                get(LISTA_USUARIOS_ENDPOINT).
        then().
            statusCode(HttpStatus.SC_OK).
            body("page", is(2)).
            //.body("per_page", is(6))
            body("data", is(notNullValue()));
           // .body("data[0].first_name", is("Michael"));

    }

    @Test
    public void testeCriarUsuarioComSucesso(){
        Usuario usuario = new Usuario("rafael", "eng test", "teste@rafael.com");

        given().
         //   contentType(ContentType.JSON).
            body(usuario).
        when().
            post(CRIAR_USUARIO_ENDPOINT).
        then().
            statusCode(HttpStatus.SC_CREATED).
            body("name", is("rafael"));
    }

}
