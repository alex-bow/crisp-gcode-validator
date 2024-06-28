// package crisp;
//
// // Currently under construction, won't do anything interesting
//
// import java.io.File;
// import java.util.ArrayList;
//
// class ToolPathDemo {
//     public static void main(String[] args) {
//         ArrayList<ParserModule> pms = new ArrayList<ParserModule>();
//         pms.add(new ToolpathParser());
//         LazyParser p = new LazyParser(new File("samples/90PercentInfill.gcode"), pms);
//         ArrayList<Validator> v = new ArrayList<Validator>();
//         v.add(new LibraryValidator());
//         ValidationBox vb = new ValidationBox(p, v);
//         vb.validate();
//     }
// }
