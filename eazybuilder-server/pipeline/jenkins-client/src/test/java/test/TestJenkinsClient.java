package test;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import com.eazybuilder.ci.jenkins.Jenkins;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.junit.Assert;
import org.junit.Test;

import com.offbytwo.jenkins.model.QueueReference;
public class TestJenkinsClient {
    static Jenkins jenkins=null;
    static boolean crumbFlag=true;//crfs crumb 高版本jenkins需要
    static{
        try {
            jenkins=new Jenkins(new URI("http://jenkinsxxxxx/jenkins/"), "admin", "xxxx");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void isPipelineDone(){
        System.out.println(
                jenkins.isPipeLineDone("NHSK_capitalpool"));
    }

    @Test
    public void addCredential(){
        try {
            String credentialId="18a52f67-e829-4b85-82f3-12977595aead";
            jenkins.addCredential("18a52f67-e829-4b85-82f3-12977595aead", "backup", "xxxxxxx@", "",crumbFlag);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void listJob() throws URISyntaxException{
        try {
            jenkins.getJobs().forEach((name,job)->{
                System.out.println(name);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void deleteJob() throws URISyntaxException{
        try {
            //add some job
//			addJob();
            //remote it
            jenkins.getJobs().forEach((name,job)->{
                System.out.println(name);
                try {
//                    jenkins.deleteJob(name,crumbFlag);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void addJob(){
        try {
            String configXml=FileUtils.readFileToString(new File("src/test/resources/config.xml"), "utf-8");
            jenkins.createJob("pipe-test", configXml,crumbFlag);
            Assert.assertNotNull(jenkins.getJob("pipe-test"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    @Test
//    public void fullTest(){
//        try {
//            addCredential();
//            deleteJob();
//            addJob();
//            runJob();
//            listJob();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    @Test
    public void runJob(){
        try {
            QueueReference qr=jenkins.getJob("pipe-test").build(crumbFlag);
            jenkins.waitUnitRun(qr.getQueueItemUrlPart());
            while(!jenkins.isPipeLineDone("pipe-test")) {
                try {
                    Thread.sleep(5*1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("DONE");
            System.out.println(ToStringBuilder.reflectionToString(
                    jenkins.getLastPipeline("pipe-test")));
            fetchCheckoutLog();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

//    @Test
//    public void runJob1(){
//        try {
//            Job job=jenkins.getJob("CI-TEST");
//            job.build(crumbFlag);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    @Test
    public void fetchCheckoutLog(){
        System.out.println(jenkins.getCheckOutRevision("pipe-test"));
    }
}
