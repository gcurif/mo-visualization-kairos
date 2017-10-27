/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kairoVisualization;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import facialAnalysisCore.FacialAnalyser;
import facialAnalysisCore.FacialAnalysis;
import facialAnalysisCore.Instant;
import facialAnalysisCore.Person;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import mo.organization.FileDescription;
import static mo.organization.ProjectOrganization.logger;
import org.apache.http.HttpEntity;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author gustavo
 */
public class KairosAnalyser extends FacialAnalyser {

    public KairosAnalyser(String urlBase, String app_id, String app_key){
        
        this.userAlias = "app_id";
        this.keyAlias = "app_key";
        this.urlBase = urlBase;
        this.user = app_id;
        this.key = app_key; 
    }
    
    @Override
    public FacialAnalysis uploadVideo(String urlOrPath) {

        FacialAnalysis analysis= new FacialAnalysis();
    
        CloseableHttpClient httpclient = HttpClients.createDefault();
        File file = new File(urlOrPath);
        

        // build multipart upload request
        HttpEntity data = MultipartEntityBuilder.create()
                    .setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
                    .addBinaryBody("source", file, ContentType.DEFAULT_BINARY, file.getName())
                    .build();
        
        // build http request and assign multipart upload data
        HttpUriRequest request = RequestBuilder
                    .post("https://api.kairos.com/v2/media")
                    .setEntity(data)
                    .build();
        
        request.setHeader(this.userAlias, this.user);
        request.setHeader(this.keyAlias,  this.key);

        
        System.out.println("Executing request " + request.getRequestLine());
        
            // Create a custom response handler
            ResponseHandler<String> responseHandler = response -> {
                int status = response.getStatusLine().getStatusCode();
                if (status >= 200 && status < 300) {
                    HttpEntity entity = response.getEntity();
                    return entity != null ? EntityUtils.toString(entity) : null;
                } else {
                   // throw new ClientProtocolException("Unexpected response status: " + status);
                   System.out.println("error: "+status);
                   return null;
                }
            };
        try {
            
            String responseBody;
            responseBody = httpclient.execute(request, responseHandler);
            System.out.println(responseBody);
            
            JSONObject jsonBody = new JSONObject(responseBody);
            System.out.println(responseBody);
            
            analysis.setId(jsonBody.getString("id"));
            
                String path = analysis.getVideoPath();
                String timePath =  path.substring(0,path.lastIndexOf(".")) + "-temp.txt";
                String cadena;

                FileReader f;
                try {
                    f = new FileReader(timePath);
                    BufferedReader b = new BufferedReader(f);
                    try {
                        if((cadena=b.readLine())!=null){
                            analysis.setStart(Long.parseLong(cadena));
                        }if((cadena=b.readLine())!=null){
                            analysis.setEnd(Long.parseLong(cadena));
                        }  
                        b.close();
                    } catch (IOException ex) {
                        logger.log(Level.SEVERE, null, ex);
                    }
                } catch (FileNotFoundException ex) {
                    logger.log(Level.SEVERE, null, ex);
                } 
                         
            return analysis;

        } catch (IOException ex) {
            Logger.getLogger(KairosAnalyser.class.getName()).log(Level.SEVERE, null, ex);
        }

        return analysis;
    }

    @Override
    public FacialAnalysis uploadVideo(File file) {
        
        FacialAnalysis analysis= new FacialAnalysis();
        CloseableHttpClient httpclient = HttpClients.createDefault();
        
        analysis.setVideoName(file.getName());
        analysis.setVideoPath(file.getPath());
        
        // build multipart upload request
        HttpEntity data = MultipartEntityBuilder.create()
                    .setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
                    .addBinaryBody("source", file, ContentType.DEFAULT_BINARY, file.getName())
                    .build();
        
        // build http request and assign multipart upload data
        HttpUriRequest request = RequestBuilder
                    .post(this.urlBase)
                    .setEntity(data)
                    .build();
        
        request.setHeader(this.userAlias, this.user);
        request.setHeader(this.keyAlias, this.key);
        
        // Create a custom response handler
        ResponseHandler<String> responseHandler = response -> {
                int status = response.getStatusLine().getStatusCode();
                if (status >= 200 && status < 300) {
                    HttpEntity entity = response.getEntity();
                    return entity != null ? EntityUtils.toString(entity) : null;
                } else {
                   // throw new ClientProtocolException("Unexpected response status: " + status);
                   System.out.println("error: "+status);
                   return null;
                }
            };
        try {
            
            String responseBody;
            responseBody = httpclient.execute(request, responseHandler);
            JSONObject jsonBody = new JSONObject(responseBody);
            System.out.println(responseBody);
            
            analysis.setId(jsonBody.getString("id"));
            
                String path = analysis.getVideoPath();
                String timePath =  path.substring(0,path.lastIndexOf(".")) + "-temp.txt";
                String cadena;

                FileReader f;
                try {
                    f = new FileReader(timePath);
                    BufferedReader b = new BufferedReader(f);
                    try {
                        if((cadena=b.readLine())!=null){
                            analysis.setStart(Long.parseLong(cadena));
                        }if((cadena=b.readLine())!=null){
                            analysis.setEnd(Long.parseLong(cadena));
                        }  
                        b.close();
                    } catch (IOException ex) {
                        logger.log(Level.SEVERE, null, ex);
                    }
                } catch (FileNotFoundException ex) {
                    logger.log(Level.SEVERE, null, ex);
                }             
            
            
            return analysis;


        } catch (IOException ex) {
            Logger.getLogger(KairosAnalyser.class.getName()).log(Level.SEVERE, null, ex);
        }        
 
        return analysis;
    }

    @Override
    public Future<FacialAnalysis> uploadVideoAsync(String urlOrPath) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Future<FacialAnalysis> uploadVideoAsync(File file) {
         ExecutorService executor = Executors.newFixedThreadPool(1);
         return executor.submit(new KairosAsync(this,"uploadVideo",file));
    }

    @Override
    public FacialAnalysis update(FacialAnalysis analysis) {
                
        try {
            HttpResponse<JsonNode>
             response = Unirest.get(this.urlBase+"/"+analysis.getId())
                    .header(this.userAlias, this.user).header(this.keyAlias , this.key)
                    .asJson();
              
            String status= response.getBody().getObject().getString("status_message");
            analysis.setStatus(status);
            analysis.setOriginalBody(response);
            
            if(!status.equals("Analyzing")){
                                 
                analysis.addPerson(new Person("anger","disgust","fear","joy","sadness","surprise"));
            
                for(int i=0; i<response.getBody().getObject().getJSONArray("frames").length(); i++){

                    JSONObject emotions = response.getBody().getObject().getJSONArray("frames").getJSONObject(i).getJSONArray("people").getJSONObject(0).getJSONObject("emotions");
                    Double time = response.getBody().getObject().getJSONArray("frames").getJSONObject(i).getDouble("time");
                    analysis.getPerson(0).getEmotion(0).addInstant(new Instant(time.longValue()+analysis.getStart(),emotions.getDouble("anger")/100));
                    analysis.getPerson(0).getEmotion(1).addInstant(new Instant(time.longValue()+analysis.getStart(),emotions.getDouble("disgust")/100));
                    analysis.getPerson(0).getEmotion(2).addInstant(new Instant(time.longValue()+analysis.getStart(),emotions.getDouble("fear")/100));
                    analysis.getPerson(0).getEmotion(4).addInstant(new Instant(time.longValue()+analysis.getStart(),emotions.getDouble("joy")/100));
                    analysis.getPerson(0).getEmotion(3).addInstant(new Instant(time.longValue()+analysis.getStart(),emotions.getDouble("sadness")/100));
                    analysis.getPerson(0).getEmotion(4).addInstant(new Instant(time.longValue()+analysis.getStart(),emotions.getDouble("surprise")/100));
                }
            }
            
        } catch (UnirestException ex) {
            Logger.getLogger(KairosAnalyser.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return analysis;
    }

    @Override
    public Future<FacialAnalysis> updateAsync(FacialAnalysis analysis) {
         ExecutorService executor = Executors.newFixedThreadPool(1);
         return executor.submit(new KairosAsync(this,"update",analysis));      }

    @Override
    public FacialAnalysis analysisFromFile(String jsonPath) {
        
        FacialAnalysis analysis = new FacialAnalysis();
        analysis.addPerson(new Person("anger","disgust","fear","joy","sadness","surprise"));
        
        FileReader fr;
        try {
           
            
            fr = new FileReader(jsonPath);
                BufferedReader br = new BufferedReader(fr);
                JSONObject jsonSource =  new JSONObject(br.readLine());
                
                if(jsonSource.getString("status").equals("incomplete")||jsonSource.getString("status").equals("En linea")){
                    FacialAnalysis a = new FacialAnalysis(this,jsonSource.getString("id"),jsonSource.getString("videoName"),jsonSource.getString("status"));
                    a.setVideoPath(jsonSource.getString("videoPath"));
                    a.setStart(jsonSource.getLong("startUnix"));
                    a.setEnd(jsonSource.getLong("endUnix"));
                    
                    return a;
                    
                }else{
                
                analysis.setVideoPath(jsonSource.getString("videoPath"));
                analysis.setStart(jsonSource.getLong("startUnix"));            
                analysis.setEnd(jsonSource.getLong("endUnix"));                    
                    
                JSONArray frames = jsonSource.getJSONArray("frames");                        
                for(int i=0; i<frames.length(); i++){

                    JSONObject emotions = frames.getJSONObject(i).getJSONArray("people").getJSONObject(0).getJSONObject("emotions");
                    Double time = frames.getJSONObject(i).getDouble("time");
                    analysis.getPerson(0).getEmotion(0).addInstant(new Instant(time.longValue()+analysis.getStart(),emotions.getDouble("anger")/100));
                    analysis.getPerson(0).getEmotion(1).addInstant(new Instant(time.longValue()+analysis.getStart(),emotions.getDouble("disgust")/100));
                    analysis.getPerson(0).getEmotion(2).addInstant(new Instant(time.longValue()+analysis.getStart(),emotions.getDouble("fear")/100));
                    analysis.getPerson(0).getEmotion(3).addInstant(new Instant(time.longValue()+analysis.getStart(),emotions.getDouble("joy")/100));
                    analysis.getPerson(0).getEmotion(4).addInstant(new Instant(time.longValue()+analysis.getStart(),emotions.getDouble("sadness")/100));
                    analysis.getPerson(0).getEmotion(5).addInstant(new Instant(time.longValue()+analysis.getStart(),emotions.getDouble("surprise")/100));
                }                
                }
              
        }catch(org.json.JSONException ex){
                   System.out.println("incompatible json");
                    //Logger.getLogger(KairosAnalyser.class.getName()).log(Level.SEVERE, null, ex);
                    return null;
                
        } catch (FileNotFoundException ex) {
            Logger.getLogger(KairosAnalyser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(KairosAnalyser.class.getName()).log(Level.SEVERE, null, ex);
        }

        return analysis;
    }

    @Override
    public boolean analysisToFile(FacialAnalysis analysis, String path) {


        if((analysis.getStatus().equals("Complete")||analysis.getStatus().equals("Completado"))&&
           analysis.getOriginalBody()!=null){
  
                FileWriter output = null;
                BufferedWriter bw = null;

                try
                {
                    output = new FileWriter(path);
                    bw = new BufferedWriter(output);

                    JSONObject outputJson = new JSONObject(analysis.getOriginalBody().getBody().toString());
                    outputJson.put("videoName", analysis.getVideoName());
                    outputJson.put("id", analysis.getId());
                    outputJson.put("videoPath", analysis.getVideoPath());
                    outputJson.put("startUnix", analysis.getStart());
                    outputJson.put("endUnix", analysis.getEnd());
                    outputJson.put("status", analysis.getStatus());

                    
                    FileDescription desc = new FileDescription(new File(path), this.getClass().getName()+".complete");
                    bw.write(outputJson.toString());
                    System.out.println("file saved in: " + path );


                    bw.close();

                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                } finally {
                   try {

                   if (null != output){
                      output.close();}
                      return true;

                   } catch (Exception e2) {
                      e2.printStackTrace();
                      return false;
                   }
                }        
        }else{
            
              JSONObject jsonOutput = new JSONObject();
              jsonOutput.put("videoName", analysis.getVideoName());
                        jsonOutput.put("id", analysis.getId());
                        jsonOutput.put("status", analysis.getStatus());
                        jsonOutput.put("videoPath", analysis.getVideoPath());
                        jsonOutput.put("startUnix", analysis.getStart());
                        jsonOutput.put("endUnix", analysis.getEnd());                           

             try
               {
                 FileWriter output = new FileWriter(path);
                 System.out.println("file saved in: " + path );

                 BufferedWriter writer = new BufferedWriter(output);
                 FileDescription desc = new FileDescription(new File(path), this.getClass().getName()+".incomplete");

                 writer.write(jsonOutput.toString());
                 writer.close();
                 output.close();
                 
                 return true;

                } catch (Exception e) {
                 e.printStackTrace();
                 return false;
                }
        }

    }
    
}
