package qaops.automation.api.teste;

import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.hamcrest.MatcherAssert;
import org.junit.Assert;
import org.junit.Test;
import qaops.automation.api.dominio.Usuario;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.*;

import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static qaops.automation.api.teste.TesteUsuarioOutraOpcao.*;

public class TesteUsuario extends TesteBase {

    private static final String LISTA_USUARIOS_ENDPOINT = "/users";
    private static final String CRIAR_USUARIO_ENDPOINT = "/user";
    private static final String MOSTRAR_USUARIO_ENDPOINT = "users/{userId}";

    @Test
    public void testeMostraPaginaEspecifica() {
        given().
                params("page", "2").
        when().
                get(LISTA_USUARIOS_ENDPOINT).
        then().
                statusCode(HttpStatus.SC_OK).
                body("page", is(2)).
                //.body("per_page", is(6))
                body("data", is(notNullValue())); //checkar se o data não é vazio
        //verificar o que tem dentro de data notNullValue, se tiver vazio é só testar verificando o NullValue
        // .body("data[0].first_name", is("Michael"));

    }

    @Test
    public void testeCriaUsuarioComSucesso() {
      //  Usuario usuario = new Usuario("rafael", "eng test", "teste@rafael.com", "lima");
        Map<String, String> usuario = new HashMap<>();
        usuario.put("name", "rafael");
        usuario.put("job", "eng teste");

        given().
                //   contentType(ContentType.JSON).
                        body(usuario).
        when().
                post(CRIAR_USUARIO_ENDPOINT).
        then().
                contentType(ContentType.JSON).

                //para esse, é possível ler o https://hc.apache.org/httpclient-legacy/apidocs/org/apache/commons/httpclient/HttpStatus.html#SC_CREATED que ficaria coerente
                        //a utilização da HttpStatus.SC_CREATED, ele entende como tendo que retornar o SC_CREATED STATUS 201
                statusCode(HttpStatus.SC_CREATED).
                body("name", is("rafael"));
    }

    @Test
    public void testeTamanhoDosItensMostradosIgualAoPerPage() {
        int paginaEsperada = 2;
        int perPageEsperado = retornaPerPageEsperado(paginaEsperada);

        given().
                params("page", paginaEsperada).
        when().
                get(LISTA_USUARIOS_ENDPOINT).
        then().
                //aqui ele retorna o 200 OK
                statusCode(HttpStatus.SC_OK).
                contentType(ContentType.JSON).
                body(
                        "page", is(paginaEsperada),
                        "data.size()", is(perPageEsperado),
                        "data.findAll { it.avatar.starWith('https://s3.amazonaws.com') },size()", is(perPageEsperado)
                        );

    }

    //pega o retorno de json e transformo em um objeto java
    @Test
    public void testeMostraUsuarioEspecifico(){
        String uri = getUri(MOSTRAR_USUARIO_ENDPOINT);

        Usuario usuario =  given().
                pathParam("userId", 2).
        when().
                get(uri).
        then().
                contentType(ContentType.JSON).
//                as(Usuario.classa);
        statusCode(HttpStatus.SC_OK).
                extract().
                //   body("data.email", containsString("@regres.in"));
                        body().jsonPath().getObject("data", Usuario.class);

        assertThat(usuario.getEmail(), containsString("@regres.in"));
        assertThat(usuario.getName(), is("Janet"));
        assertThat(usuario.getLastName(), is("Weaver"));

    }


    private int retornaPerPageEsperado(int page) {
        String uri = getUri(LISTA_USUARIOS_ENDPOINT);

        return given().
                param("page", 2).
             when().
                get(uri).
             then().
                contentType(ContentType.JSON).
                statusCode(HttpStatus.SC_OK).
             extract().
                path("per_page");
    }

//    private String getUri(String endpoint){
//        return BASE_URL + BASE_PATH + endpoint;
//    }
}