package self.boltclone.containerserver.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public class PatchGenerator {

    private final ObjectMapper objectMapper = new ObjectMapper();

    Map<String, String> fileLineCounts = Map.of(
        "src/App.jsx", "function App() {\n  return <>Hi</>;\n}\n\nexport default App;",
        "src/main.jsx",
        "import { StrictMode } from 'react'\nimport { createRoot } from 'react-dom/client'\nimport './index.css'\nimport App from './App.jsx'\ncreateRoot(document.getElementById('root')).render(\n  <StrictMode>\n    <App />\n  </StrictMode>,\n)"
    );

    public Map<String, String> parseCode(String codeJson) throws Exception {

        JsonNode codeNode = objectMapper.readTree(codeJson);
        return objectMapper.convertValue(codeNode, Map.class);
    }

    public String generateGitPatch(Map<String, String> codeMap) {

        StringBuilder patchBuilder = new StringBuilder();

        for (Map.Entry<String, String> entry : codeMap.entrySet()) {
            String filePath = entry.getKey();
            String newContent = entry.getValue();

            patchBuilder.append("diff --git a/")
                .append(filePath)
                .append(" b/")
                .append(filePath)
                .append("\n");

            String[] lines = newContent.split("\n");
            String[] oldLines = fileLineCounts.getOrDefault(filePath, "").split("\n");

            if (fileLineCounts.containsKey(filePath)) {
                patchBuilder.append("--- a/").append(filePath).append("\n");
                patchBuilder.append("+++ b/").append(filePath).append("\n");
                patchBuilder.append("@@ -1,").append(oldLines.length);
                patchBuilder.append(" +1,").append(lines.length).append(" @@\n");
                for (String line : oldLines)
                    patchBuilder.append("-").append(line).append("\n");
            } else {
                patchBuilder.append("new file mode 100644\n");
                patchBuilder.append("--- /dev/null\n");
                patchBuilder.append("+++ b/").append(filePath).append("\n");
                patchBuilder.append("@@ -0,0");
                patchBuilder.append(" +1,").append(lines.length).append(" @@\n");
            }

            for (String line : lines) {
                patchBuilder.append("+").append(line).append("\n");
            }
        }

        return patchBuilder.toString();
    }

}
