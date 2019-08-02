package ar.com.tandilweb.room.orchestratorBridge.processors;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;

@Component
public class BackwardValidationProcessor extends OrchestratorGenericProcessor {

	public void processBVDataChallengeSchema(String schemaBody) throws JsonProcessingException {

	}

	public void processBVInvalidSchema(String schemaBody) throws JsonProcessingException {

	}

	public void processBVUnknownSchema(String schemaBody) throws JsonProcessingException {

	}
}
