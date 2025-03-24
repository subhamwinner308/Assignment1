package com.browserstack;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class BrowserStackAuto {
	
	// Method to download the image
    private static void downloadImage(String imageUrl, String targetPath) {
        try (InputStream in = new URL(imageUrl).openStream();
             BufferedInputStream bis = new BufferedInputStream(in);
             FileOutputStream fos = new FileOutputStream(targetPath)) {

            byte[] buffer = new byte[1024];
            int bytesRead;

            //Read and write the image data
            while ((bytesRead = bis.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }

            System.out.println("Imagen descargada: " + targetPath);
        } catch (IOException e) {
            System.out.println("Error al descargar la imagen: " + e.getMessage());
        }
    }
    
 // Method to translate text using Google Translate API
    private static String translateText(String text, String sourceLanguage, String targetLanguage) {
        Translate translate = TranslateOptions.getDefaultInstance().getService();
        Translation translation = translate.translate(
                text,
                Translate.TranslateOption.sourceLanguage(sourceLanguage),
                Translate.TranslateOption.targetLanguage(targetLanguage)
        );
        return translation.getTranslatedText();
    }

	public static void main(String[] args) throws MalformedURLException, IOException {
		
		System.setProperty("webdriver.chrome.driver", "C:\\subham.saha 09-12-2024\\RESTORED FILES\\SELENIUM\\BrowserDrivers\\ChromeDriver\\chromedriver.exe");
		
		WebDriver driver = new ChromeDriver();
		
		driver.manage().window().maximize();
		
		driver.manage().timeouts().implicitlyWait(10,TimeUnit.SECONDS);
		
		driver.get("https://elpais.com/");
		
		driver.findElement(By.ByXPath.id("didomi-notice-agree-button")).click();
		
		//
		try {
			//Navigate to the Opinion section of the website and Printing the Title and Content of first 5 articles
			List<WebElement> articles = driver.findElements(By.xpath("//section[header//a[contains(text(),'Opinión')]]/div/div/article"));
			
			HashMap<String, Integer> countHeaders = new HashMap<>();
			
			for (int i = 0; i < 5 && i < articles.size(); i++) {
				
					WebElement article = articles.get(i);
		            
		            //Fetch the title
		            WebElement title = article.findElement(By.tagName("h2"));
		            String titleText = title.getText();
		            
		            //Print the title of the article
		            System.out.println("Article " + (i + 1) + " Title in Spanish: " + titleText);
		            
		            //Translate the title and content to English
	                String translatedTitle = translateText(title, "es", "en");
	                
	                //System.out.println("Article " + (i + 1) + " Title in English: " + translatedTitle);
	                
	                String[] words = translatedTitle.split("\\s+");
	                
	                for (String word : words) {
	                    // Convert the word to lower case for case-insensitive counting
	                    word = word.toLowerCase();
	                    
	                    // Update the word count in the HashMap
	                    countHeaders.put(word, countHeaders.getOrDefault(word, 0) + 1);
	                }
	                
		            try {
	            	//Fetch the Content
		            WebElement content = article.findElement(By.tagName("p"));
		            String contentText = content.getText();
		            
		            //Print the Content of the article
		            System.out.println("Article " + (i + 1) + " Content in Spanish: " + contentText);
		            System.out.println("------------------------------------------------");
		            }catch(Exception e) {
		            	System.out.println("Error" + e.getMessage());
		            	
		            	System.out.println("Article " + (i + 1) + " image is saved!");
		            	
		    			//Check if there's a cover image and download it if available
		                WebElement imgElement = article.findElement(By.tagName("figure")).findElement(By.tagName("a")).findElement(By.tagName("img"));
		                String imageUrl = imgElement.getAttribute("src");
		                
		                if (imageUrl != null && !imageUrl.isEmpty()) {
		                	
		                    //Download and save the cover image of the article to the local machine
		                    downloadImage(imageUrl, "Downloads\\image_"+i+1+".jpg");
		                }
		    		}
		            
		            System.out.println("From the translated headers, words that are repeated more than twice across all headers combined:");
		            for (Map.Entry<String, Integer> entry : countHeaders.entrySet()) {
		                if (entry.getValue() > 2) {  
		                    System.out.println(entry.getKey() + ": " + entry.getValue());
		                }
		            }
		     
            }
			
		}finally {
            // Close the browser
            driver.quit();
        }

	}

}
