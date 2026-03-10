// package com.codej.web.cached;

// import java.io.ByteArrayInputStream;
// import java.io.IOException;

// import javax.servlet.ReadListener;
// import javax.servlet.ServletInputStream;

// public class CachedServletInputStream extends ServletInputStream {
//     private final ByteArrayInputStream inputStream;

//     public CachedServletInputStream(ByteArrayInputStream inputStream) {
//         this.inputStream = inputStream;
//     }

//     @Override
//     public boolean isFinished() {
//         return inputStream.available() == 0;
//     }

//     @Override
//     public boolean isReady() {
//         return true;
//     }

//     @Override
//     public void setReadListener(ReadListener readListener) {
//         throw new UnsupportedOperationException();
//     }

//     @Override
//     public int read() throws IOException {
//         return inputStream.read();
//     }
// }
