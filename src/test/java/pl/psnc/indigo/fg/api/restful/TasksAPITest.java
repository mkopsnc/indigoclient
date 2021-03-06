package pl.psnc.indigo.fg.api.restful;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.Assert;
import static junit.framework.Assert.fail;
import org.junit.Test;
import pl.psnc.indigo.fg.api.restful.TasksAPI;
import pl.psnc.indigo.fg.api.restful.BaseAPI;
import pl.psnc.indigo.fg.api.restful.jaxb.InputFile;
import pl.psnc.indigo.fg.api.restful.jaxb.OutputFile;
import pl.psnc.indigo.fg.api.restful.jaxb.Task;
import pl.psnc.indigo.fg.api.restful.jaxb.Upload;

public class TasksAPITest {

  @Test
  public void testCreateTask() {
    TasksAPI api = new TasksAPI(BaseAPI.LOCALHOST_ADDRESS);
    try {
      Task newTask = new Task();
      newTask.setUser("brunor");
      newTask.setApplication("1");
      newTask.setDescription("hello");
      Task result = api.createTask(newTask);

      // Check the status of this task
      result = api.getTask(result);

    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  @Test
  public void testFileAccess() {
    try {
      String fileName = getClass().getResource("/sayhello.sh").getFile();
//                URI uri = ClassLoader.getSystemResource("/sayhello.sh").toURI(); 
      File shFile = new File(fileName);
    } catch (Exception ex) {
      Logger.getLogger(TasksAPITest.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  @Test
  public void testSubmitTaskWithFiles() {

    TasksAPI api = new TasksAPI(BaseAPI.LOCALHOST_ADDRESS);
    Task result = null;
    try {
      Task newTask = new Task();
      newTask.setUser("brunor");
      newTask.setApplication("2");
      newTask.setDescription("Test with files");

      ArrayList<String> arguments = new ArrayList<String>();
      arguments.add("I am saying hello");

      ArrayList<OutputFile> outputFiles = new ArrayList<OutputFile>();
      OutputFile oFile = new OutputFile();
      oFile.setName("sayhello.data");
      outputFiles.add(oFile);

      ArrayList<InputFile> inputFiles = new ArrayList<InputFile>();
      InputFile iFileSH = new InputFile();
      iFileSH.setName("sayhello.sh");

      InputFile iFileTXT = new InputFile();
      iFileTXT.setName("sayhello.txt");

      inputFiles.add(iFileSH);
      inputFiles.add(iFileTXT);

      newTask.setOutput_files(outputFiles);
      newTask.setInput_files(inputFiles);

      newTask.setArguments(arguments);
      result = api.createTask(newTask);
    } catch (Exception ex) {
      ex.printStackTrace();
      fail("Error while creating task");
    }

    // Once task is created, we can upload files
    try {
      String url = result.getUploadURLAsString();
      String fileNameSH = getClass().getResource("/sayhello.sh").getFile();
      String fileNameTXT = getClass().getResource("/sayhello.txt").getFile();
      api.uploadFileForTask(result, url, new File(fileNameSH));
      api.uploadFileForTask(result, url, new File(fileNameTXT));
    } catch (Exception ex) {
      ex.printStackTrace();
      fail("Error while creating task");
    }
  }
  
  /**
   * This test is a complete scenario where job is submitted, executed
   * and all outputs are retrieved.
   */
  @Test
  public void testSubmitTaskWithFilesWaitGetOutputs() {

    TasksAPI api = new TasksAPI(BaseAPI.LOCALHOST_ADDRESS);
    Task result = null;
    try {
      Task newTask = new Task();
      newTask.setUser("brunor");
      newTask.setApplication("2");
      newTask.setDescription("Test with files");

      ArrayList<String> arguments = new ArrayList<String>();
      arguments.add("I am saying hello");

      ArrayList<OutputFile> outputFiles = new ArrayList<OutputFile>();
      OutputFile oFile = new OutputFile();
      oFile.setName("sayhello.data");
      outputFiles.add(oFile);

      ArrayList<InputFile> inputFiles = new ArrayList<InputFile>();
      InputFile iFileSH = new InputFile();
      iFileSH.setName("sayhello.sh");

      InputFile iFileTXT = new InputFile();
      iFileTXT.setName("sayhello.txt");

      inputFiles.add(iFileSH);
      inputFiles.add(iFileTXT);

      newTask.setOutput_files(outputFiles);
      newTask.setInput_files(inputFiles);

      newTask.setArguments(arguments);
      result = api.createTask(newTask);

      // Once task is created, we can upload files
      String url = result.getUploadURLAsString();
      String fileNameSH = getClass().getResource("/sayhello.sh").getFile();
      String fileNameTXT = getClass().getResource("/sayhello.txt").getFile();
      api.uploadFileForTask(result, url, new File(fileNameSH));
      api.uploadFileForTask(result, url, new File(fileNameTXT));
      
      // We can check status and wait for "DONE"
      String status = null;
      int retry = 100;
      
      do {
        Task tmp = api.getTask(result);
        status = tmp.getStatus();
        Thread.sleep(5000);
        retry--;
      } while(!status.equals("DONE") && retry > 0);
      
      if(retry == 0) {
        fail("To many retries");
      }
      
      ArrayList<OutputFile> files = api.getOutputsForTask(result);
      
      String outputDir = getClass().getResource("/outputs").getFile();
      
      for( OutputFile f : files ){
        api.downloadOutputFile(f, outputDir);
      }
      
    } catch (Exception ex) {
      ex.printStackTrace();
      fail("Error while creating task");
    }
    
    
  }

  @Test
  public void testGetTask() {
    TasksAPI api = new TasksAPI(BaseAPI.LOCALHOST_ADDRESS);

    // TODO: make sure to set proper task ID below
    // this one is an arbitrary value from previous calls to
    // TaskAPI
    try {
      Task newTask = new Task();
      newTask.setUser("brunor");
      newTask.setId("1");

      Task task = api.getTask(newTask);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  @Test
  public void testGetAllTasks() {
    TasksAPI api = new TasksAPI(BaseAPI.LOCALHOST_ADDRESS);

    // TaskAPI
    try {
      Task[] tasks = api.getAllTasks();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  @Test
  public void testFinalize() {
    System.out.flush();
  }
}
