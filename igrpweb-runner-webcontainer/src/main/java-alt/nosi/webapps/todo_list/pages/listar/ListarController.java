package nosi.webapps.todo_list.pages.listar;

import java.io.IOException;
import nosi.core.webapp.Controller;
import nosi.core.webapp.HttpStatus;
import nosi.core.webapp.Response;

/**
 *
 * @author poolb
 */
public class ListarController  extends Controller {
    
    public Response actionIndex() throws IOException, IllegalArgumentException, IllegalAccessException{
    
        Response resp = new Response();
        resp.setType(1);
        resp.setCharacterEncoding(Response.CHARSET_UTF_8);
        resp.setContentType(Response.FORMAT_TEXT);
        resp.setHttpStatus(HttpStatus.STATUS_200);
        
        resp.setContent("Pagina de listagem de todo");
        return resp;
    }
}
