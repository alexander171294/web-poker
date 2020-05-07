package ar.com.tandilweb.room_int;

import ar.com.tandilweb.exchange.ServerRecordingSchema;

public interface OrchestratorPipe {
	public boolean SendToOrchestrator(ServerRecordingSchema srs);
}
