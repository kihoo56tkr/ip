# AI Usage Record - Mintel Project

## Tool Used
- **Primary AI Assistant**: Deepseek
- **Period**: January - February 2026
- **Purpose**: Code quality improvement, debugging, and feature implementation

---

## Increment: A-AiAssisted

### What I Used AI For
- Refactoring long methods (>40 lines) into smaller, focused methods
- Identifying methods that violated the Single Level of Abstraction Principle
- Extracting nested logic into separate private methods
- Generating appropriate method names for extracted code blocks

### Examples
1. **DialogBox.java**: Broke down 48-line constructor into:
    - `validateInputs()`
    - `loadFxml()`
    - `setupImageClipping()`
    - `setDialogText()`

2. **Storage.java**: Decomposed `isFileFormatCorrect()` (47 lines) into:
    - `validateFileContent()`
    - `isLineFormatValid()`
    - `validateTaskFormat()`
    - `validateTodoFormat()`, `validateDeadlineFormat()`, `validateEventFormat()`

3. **Task.java**: Refactored `fromFileString()` (58 lines) into:
    - `validateFileString()`
    - `parseAndTrimParts()`
    - `parseStatus()`
    - `createTaskByType()` and type-specific creators

### What Worked Well
- ✅ AI excelled at identifying logical breakpoints in long methods
- ✅ Generated meaningful method names that reflected the extracted logic
- ✅ Preserved all assertions and error handling during extraction
- ✅ Maintained original functionality while improving readability
- ✅ SLAP principles were applied consistently across all refactored classes

### Interesting Observations
- Methods that combined validation, processing, and formatting were the best candidates for SLAP refactoring
- Method length decreased by an average of 60-70% after refactoring

### Overall Reflection
The SLAP refactoring significantly improved code maintainability. The AI was particularly helpful in suggesting logical extraction points and maintaining consistency across similar refactoring patterns. The biggest challenge was knowing when to stop extracting - not every 5-line block needs its own method. The principle of "one level of abstraction per method" was a good guiding rule.