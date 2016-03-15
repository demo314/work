package com.demo;

import java.net.MalformedURLException;
import java.net.URL;


import java.util.List;

import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.api.runtime.process.ProcessInstance;
import org.kie.api.task.TaskService;
import org.kie.api.task.model.TaskSummary;
import org.kie.remote.client.api.RemoteRuntimeEngineFactory;

public class CustomerRegistration {

	public static void main(String[] arg){
		try {
			// Get runtime engine
			RuntimeEngine engine = RemoteRuntimeEngineFactory.newRestBuilder()
					.addUrl(new URL("http://localhost:8080/business-central/"))
					.addUserName("bpmsAdmin").addPassword("admin123!")
					.addDeploymentId("Cognizant:HumanTask:1.0")
					.build();
			//getting KieSession & TaskService
			KieSession ksession = engine.getKieSession();
			/*TaskService taskService = engine.getTaskService();*/
			//starting a process
			ProcessInstance processInstance = ksession.startProcess("humanTask.humanTask");



			System.out.println("Process Id >: "+processInstance.getId());
			System.out.println("Process State >: "+processInstance.getState());

			/*	taskService.getTasksAssignedAsPotentialOwner("bpmsAdmin", "en-UK");

					taskService.start(taskId,bpmsAdmin);*/


			TaskService taskService = engine.getTaskService();
			List<TaskSummary> taskSumList
			= taskService.getTasksAssignedAsPotentialOwner("bpmsAdmin", "en-UK");
			TaskSummary taskSum = null;
			for( TaskSummary taskSumElem : taskSumList ) {
				if( taskSumElem.getProcessInstanceId().equals(processInstance.getId()) ) {
					taskSum = taskSumElem;
				}
			}
			long taskId = taskSum.getId();
			taskService.start(taskId,"bpmsAdmin");


		} catch (MalformedURLException e) {


			e.printStackTrace();
		}

	}
}


