package com.codej.mq.config;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

import com.codej.base.utils.FileUtil;
import com.codej.mq.properties.RabbitMQProperites;

import io.arivera.oss.embedded.rabbitmq.EmbeddedRabbitMq;
import io.arivera.oss.embedded.rabbitmq.EmbeddedRabbitMqConfig;
import lombok.extern.slf4j.Slf4j;

/**
 * 로컬 환경일경우 내장 RabbitMQ가 실행된다.
 * https://github.com/AlejandroRivera/embedded-rabbitmq
 * 
 * 아래 에러 발생
 * java.io.IOException: Cannot run program "erl": CreateProcess error=2, 지정된 파일을 찾을 수 없습니다
 *  => erlang 이 설치 되지 않아서 그렇다. 
 * 
 * StartupException: Could not confirm RabbitMQ Server initialization completed successfully within 3000ms 에러발생
 * java.net.SocketException: socket closed 에러 발생
 *  => 원인 모르겠음. 사용 안함으로 설정함.
 * 
 * "Minimum required Erlang version not found. Expected '22.3' or higher."
 * download OTP 24.0 Windows 64-bit Binary File 
 * http://erlang.org/download/otp_win64_24.0.exe 
 * 
 * 
 * Could not download 'https://github.com/rabbitmq/rabbitmq-server/releases/download/v3.8.14/rabbitmq-server-windows-3.8.14.zip' to 'C:\Users\dimmby\.embeddedrabbitmq\rabbitmq-server-windows-3.8.14.zip'
 * 네트워크가 되면 downloadTarget 디렉토리에 다운로드 하고 서버를 설치한다
 */
@Slf4j
@Configuration
@ConditionalOnProperty(name="spring.rabbitmq.embedded-server-start", havingValue="true")
public class EmbededRabbitMQConfiguration {

    @Autowired
    private RabbitMQProperites rabbitMqProperites;
    
    private EmbeddedRabbitMq server;
    @PostConstruct
    public void startServer() {

        try {
            EmbeddedRabbitMqConfig config = new EmbeddedRabbitMqConfig.Builder()
                    // .downloadTarget(downloadPath)
                    .extractionFolder(FileUtil.mkDirs("rabbitmq\\servers"))
                    .useCachedDownload(false)
                    .deleteDownloadedFileOnErrors(false)
                    .port(rabbitMqProperites.getPort())
                    .build();
             
            server = new EmbeddedRabbitMq(config);
            server.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PreDestroy
    public void stopServer() {
        if (server != null) {
            server.stop();
        }
    }
}
