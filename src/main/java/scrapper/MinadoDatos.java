package scrapper;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


public class MinadoDatos {
    
    protected WebDriver driver;
    
    public MinadoDatos(WebDriver driver) { 
        this.driver = driver;  
    }

    protected void esperarSegundos(int segundos) {
        try {
            Thread.sleep(segundos * 1000);
        } catch (InterruptedException e) {}
    }

    protected void esperarFinCargaPagina() {
        WebDriverWait wait = new WebDriverWait(this.driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.jsReturnsValue("return document.readyState === 'complete';"));
    }
 
    protected String scrapyTextV2(WebElement elementoBase, String selector) {
        try {
            WebElement elemento = elementoBase.findElement(By.xpath(selector));
            String[] texto = elemento.getText().split("\\n");
            return texto[0];
        } catch (NoSuchElementException e) {
            return "";
        }
    }

    public void extraerDato(WebElement elementoBase, String selector, Consumer<String> setter) {
        try {
            String dato = scrapyTextV2(elementoBase, selector);
            setter.accept(dato);
        } catch (NoSuchElementException e) {}
    }

    protected String obtenerLink(WebElement elementoBase) {

        WebElement enlace = elementoBase.findElement(By.tagName("a"));
        String cadenaSalida = enlace.getAttribute("href");

        return cadenaSalida;
    }

    protected List<String> obtenerLinksUsuariosLinkedIn() {

        List<WebElement> enlaces = driver.findElements(By.tagName("a"));
        List<String> elementosValidos = new ArrayList<>();

        for (WebElement enlace : enlaces) {
            String url = enlace.getAttribute("href");
            if (url != null && url.contains("https://www.linkedin.com/in/")) {
                char primerChar = url.charAt(28);

                if (Character.isLowerCase(primerChar)) {
                    elementosValidos.add(url);
                }
            }
        }

        List<String> arregloFinal = eliminarDuplicados(elementosValidos);

        return arregloFinal;
    }

    private List<String> eliminarDuplicados(List<String> lista) {
        Set<String> conjunto = new HashSet<>(lista);
        return new ArrayList<>(conjunto);
    }
    
   
    
    

}
