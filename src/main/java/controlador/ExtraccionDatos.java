package controlador;

import lombok.NoArgsConstructor;
import modelo.Usuario;
import org.openqa.selenium.WebDriver;
import scrapper.ObtenerEducacion;
import automata.AutomataDatos;
import database.BusquedaDatos;
import database.InserccionDatos;
import java.util.List;
import modelo.LinkUsuario;
import org.openqa.selenium.chrome.ChromeDriver;
import scrapper.ObtenerDatosCabecera;
import scrapper.ObtenerExperiencia;

@NoArgsConstructor
public class ExtraccionDatos {
    
    
    public void MinadoUsuariosTotal(ControladorMaestro controler, Integer numeroLinksBuscar){
        
        InserccionDatos db = new InserccionDatos();
        BusquedaDatos busqueda = new BusquedaDatos();
        db.eliminarDuplicadosPorUrlUsuario();
        List<LinkUsuario> documentos = busqueda.obtenerDocumentos(numeroLinksBuscar);
     
        for (LinkUsuario elemento : documentos) {
            
            WebDriver newDriver = new ChromeDriver();
            controler.inyectarCookies(newDriver);
 
            newDriver.get(elemento.getUrlUsuario());
            
            Usuario usuario = this.PerfilCompleto(newDriver);
            System.out.println(usuario);
            db.InsertarDocumento(usuario);
            db.marcarDocumentoComoVisitado(elemento.get_id());
            
            newDriver.close(); 
        }
    }
    
    private Usuario PerfilCompleto(WebDriver driver) {
        AutomataDatos movilizador = new AutomataDatos(driver);
        movilizador.busquedaIndicesSeccionesMain();
        
        ObtenerDatosCabecera obtenerCabecera = new ObtenerDatosCabecera(driver);
        ObtenerExperiencia obtenerExperiencia = new ObtenerExperiencia(driver, movilizador);
        ObtenerEducacion obtenerEducacion = new ObtenerEducacion(driver, movilizador);
        
        
        Usuario usuario = new Usuario.UsuarioBuilder()
                    .informacionPersonal(obtenerCabecera.seccionCabcecera())
                    .experienciaLaboral(obtenerExperiencia.seccionExperiencia())
                    .educacion(obtenerEducacion.seccionEducacion())
                    .build();
        
        return usuario;
    }

}
