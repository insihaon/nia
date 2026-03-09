package com.nia.korenrca.service.cipher.rsa;

import com.nia.korenrca.service.cipher.tea.TEA;
import com.nia.korenrca.service.util.Base64;
import io.vertx.core.Handler;
import io.vertx.core.MultiMap;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

import java.net.URLDecoder;


public class RSATest {

    public static void main(String[] args) {
        RSA.RSAKey rsaKey = RSA.createRsa();

        VertxOptions options = new VertxOptions();
        options.setMaxEventLoopExecuteTime(Long.MAX_VALUE);
        Vertx vertx = Vertx.vertx(options);
        Router router = Router.router(vertx);

        router.route().handler(BodyHandler.create());
        router.get("/rsa/:value").handler(new Handler<RoutingContext>() {

            @Override
            public void handle(RoutingContext context) {
                MultiMap params = context.request().params();
                @SuppressWarnings("deprecation")
                String value = URLDecoder.decode(params.get("value"));
                System.out.println("value: " + value);

                String result = "value: " + value + "<br><br>";

                try {
                    /* 암호화된 text를 decrypt 한다. */
                    result += RSA.decryptRsa(rsaKey, value);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                context.response().putHeader("Cache-Control", "no-cache, no-store, max-age=0, must-revalidate")
                    .putHeader("Pragma", "no-cache").putHeader("Expires", "0")
                    .putHeader("Content-Type", "text/html; charset=utf-8");
                context.response().end(result);
            }
        });

        router.get("/tea/:rsaEncryptTeaKey/:teaEncryptValue").handler(new Handler<RoutingContext>() {

            @Override
            public void handle(RoutingContext context) {
                MultiMap params = context.request().params();
                String encryptKey = params.get("rsaEncryptTeaKey");
                String encryptValue = Base64.decodeString(params.get("teaEncryptValue"));
                System.out.println("key: " + encryptKey);
                System.out.println("value: " + encryptValue);

                String result = "rsaEncryptTeaKey: " + encryptKey + "<br>";
                result += "teaEncryptValue: " + encryptValue + "<br><br>";

                /* TEA 암호화에 사용된 키를 구한다 */
                String teaKey = null;
                try {
                    teaKey = RSA.decryptRsa(rsaKey, encryptKey);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                /* 암호화된 text를 TEA키를 이용하여 decrypt 한다. */
                TEA tea = new TEA(teaKey);
                String contents = tea.decrypt(encryptValue);

                result += contents;

                context.response().putHeader("Cache-Control", "no-cache, no-store, max-age=0, must-revalidate")
                    .putHeader("Pragma", "no-cache").putHeader("Expires", "0")
                    .putHeader("Content-Type", "text/html; charset=utf-8");
                context.response().end(result);
            }
        });

        vertx.createHttpServer().requestHandler(router::accept)
            // .requestHandler(req -> req.response().end("Hello World!"))
            .listen(8080, handler -> {
                if (handler.succeeded()) {
                    System.out.println("http://localhost:8080/");
                    System.out.println("" + rsaKey.getPublicKeyModulus());
                    System.out.println("" + rsaKey.getPublicKeyExponent());

                } else {
                    System.err.println("Failed to listen on port 8080");
                    System.exit(0);
                }
            });
    }
}
