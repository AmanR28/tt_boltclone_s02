package self.boltclone.aiserver.client.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import self.boltclone.aiserver.client.AiClient;

@Slf4j
@Component
@Primary
public class MockAiClient implements AiClient {

    @Override
    public String prompt(String prompt) {

        return """
{
  "summary": "Implemented a basic counter with increment and decrement functionality.",
  "code": {
    "src/App.jsx": "import { useState } from 'react';\\nfunction App() {\\n\\tconst [count, setCount] = useState(0);\\n\\treturn (\\n\\t\\t<div>\\n\\t\\t\\t<h1>Counter</h1>\\n\\t\\t\\t<p>Count: {count}</p>\\n\\t\\t\\t<button onClick={() => setCount(count - 1)}>Decrement</button>\\n\\t\\t\\t<button onClick={() => setCount(count + 1)}>Increment</button>\\n\\t\\t</div>\\n\\t);\\n}\\nexport default App;",
    "src/index.css": "body { font-family: sans-serif; }\\n\\nh1 { color: blue; }"
  }
}
            """;
    }

}
