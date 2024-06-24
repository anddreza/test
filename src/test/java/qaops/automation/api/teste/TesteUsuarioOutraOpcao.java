package qaops.automation.api.teste;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.BeforeClass;
import org.junit.Test;
import qaops.automation.api.dominio.Usuario;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class TesteUsuarioOutraOpcao extends TesteBase {
    private static final String BASE_URL = "https://regres.in";
    private static final String BASE_PATH = "https://api";


    private static final String LISTA_USUARIOS_ENDPOINT = "/users";
    private static final String CRIAR_USUARIO_ENDPOINT = "/user";
    private static final String MOSTRAR_USUARIO_ENDPOINT = "users/{userId}";

    @BeforeClass
    public static void setup(){
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @Test
    public void testeMostraPaginaEspecifica() {
        String uri = getUri(LISTA_USUARIOS_ENDPOINT);
        given().
                params("page", "2").
        when().
                get(uri).
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
        String uri = getUri(CRIAR_USUARIO_ENDPOINT);


        //  Usuario usuario = new Usuario("rafael", "eng test", "teste@rafael.com", "lima");
        Map<String, String> usuario = new HashMap<>();
        usuario.put("name", "rafael");
        usuario.put("job", "eng teste");

        given().
                   contentType(ContentType.JSON).
                        body(usuario).
        when().
                post(uri).
        then().
                //para esse, é possível ler o https://hc.apache.org/httpclient-legacy/apidocs/org/apache/commons/httpclient/HttpStatus.html#SC_CREATED que ficaria coerente
                        //a utilização da HttpStatus.SC_CREATED, ele entende como tendo que retornar o SC_CREATED STATUS 201
                statusCode(HttpStatus.SC_CREATED).
                body("name", is("rafael"));
    }

    @Test
    public void testeTamanhoDosItensMostradosIgualAoPerPage() {
        String uri = getUri(LISTA_USUARIOS_ENDPOINT);


        int paginaEsperada = 2;
        int perPageEsperado = retornaPerPageEsperado(paginaEsperada);

        given().
                params("page", paginaEsperada).
        when().
                get(uri).
        then().
                //aqui ele retorna o 200 OK
                statusCode(HttpStatus.SC_OK).
                body(
                        "page", is(paginaEsperada),
                        "data.size()", is(perPageEsperado),
                        "data.findAll { it.avatar.starWith('https://s3.amazonaws.com') },size()", is(perPageEsperado)
                        );

    }

    //pega o retorno de json e transformo em um objeto java
    @Test
    public void testeMostraUsuarioEspecifico(){
        Usuario usuario =  given().
                pathParam("userId", 2).
        when().
                get(MOSTRAR_USUARIO_ENDPOINT).
        then().
//                as(Usuario.class);
        statusCode(HttpStatus.SC_OK).
                extract().
                //   body("data.email", containsString("@regres.in"));
                        body().jsonPath().getObject("data", Usuario.class);

        assertThat(usuario.getEmail(), containsString("@regres.in"));
        assertThat(usuario.getName(), is("Janet"));
        assertThat(usuario.getLastName(), is("Weaver"));

    }


    private int retornaPerPageEsperado(int page) {
        return given().
                param("page", 2).
             when().
                get(LISTA_USUARIOS_ENDPOINT).
             then().
                statusCode(HttpStatus.SC_OK).
             extract().
                path("per_page");
    }

    static String getUri(String endpoint) {
        String uri = BASE_URL + BASE_PATH + endpoint;
        return uri;
    }

}