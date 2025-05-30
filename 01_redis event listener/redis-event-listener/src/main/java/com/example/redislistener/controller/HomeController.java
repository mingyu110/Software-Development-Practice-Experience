package com.example.redislistener.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {

    @GetMapping("/")
    @ResponseBody
    public String home() {
        return """
                <!DOCTYPE html>
                <html>
                <head>
                    <title>Redis Event Listener</title>
                    <style>
                        body {
                            font-family: Arial, sans-serif;
                            margin: 0;
                            padding: 20px;
                            line-height: 1.6;
                            color: #333;
                        }
                        .container {
                            max-width: 800px;
                            margin: 0 auto;
                            padding: 20px;
                            background-color: #f5f5f5;
                            border-radius: 5px;
                            box-shadow: 0 2px 5px rgba(0,0,0,0.1);
                        }
                        h1 {
                            color: #e74c3c;
                        }
                        code {
                            background-color: #eee;
                            padding: 2px 5px;
                            border-radius: 3px;
                            font-family: monospace;
                        }
                        pre {
                            background-color: #eee;
                            padding: 10px;
                            border-radius: 5px;
                            overflow-x: auto;
                        }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <h1>Redis Event Listener</h1>
                        <p>这是一个演示Redis键空间通知的Spring Boot应用程序。</p>
                        
                        <h2>测试API</h2>
                        <ul>
                            <li><strong>设置带过期时间的键：</strong>
                                <pre>curl -X POST "http://localhost:8080/api/cache?key=test-key&value=test-value&expirySeconds=10"</pre>
                            </li>
                            <li><strong>获取缓存值：</strong>
                                <pre>curl -X GET "http://localhost:8080/api/cache/test-key"</pre>
                            </li>
                            <li><strong>删除缓存：</strong>
                                <pre>curl -X DELETE "http://localhost:8080/api/cache/test-key"</pre>
                            </li>
                        </ul>
                        
                        <h2>过期事件监听</h2>
                        <p>设置一个短时间过期的键，然后观察控制台输出。键过期后，应用将打印类似以下内容：</p>
                        <pre>Key expired: test-key
Business logic triggered for expired key: test-key</pre>
                    </div>
                </body>
                </html>
                """;
    }
} 