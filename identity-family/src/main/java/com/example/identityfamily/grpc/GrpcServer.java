package com.example.identityfamily.grpc;

import com.example.identityfamily.core.domain.child.ChildRepository;
import com.example.identityfamily.core.domain.parent.ParentRepository;
import com.example.identityfamily.core.domain.parentchild.ParentChildRepository;
import io.grpc.Server;
import io.grpc.netty.shaded.io.grpc.netty.NettyServerBuilder;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@Slf4j
public class GrpcServer {

    private final ParentChildRepository parentChildRepository;
    private final ChildRepository childRepository;
    private final ParentRepository parentRepository;

    @Value("${grpc.server.port:9090}")
    private int grpcPort;

    private Server server;

    @PostConstruct
    public void start() throws IOException {
        server = NettyServerBuilder.forPort(grpcPort)
                .addService(new FamilyServiceImpl(parentChildRepository, childRepository, parentRepository))
                .build()
                .start();
        log.info("gRPC server started on port {}", grpcPort);
        
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.info("Shutting down gRPC server...");
            GrpcServer.this.stop();
        }));
    }

    @PreDestroy
    public void stop() {
        if (server != null) {
            log.info("Stopping gRPC server...");
            server.shutdown();
            try {
                server.awaitTermination(30, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error("Interrupted while waiting for server shutdown", e);
            }
        }
    }
}
