package crisp;

// Base class for parser modules that perform additional processing or form additional structures on a GCode 
// file beyond LazyParser
abstract class ParserModule {
    void parseLine(Line line) {
    }
}
