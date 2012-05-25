 

JLHTTP - Java Lightweight HTTP Server 1.1
=========================================

(c) copyright 2005-2009 Amichai Rothman



1. What is the Java Lightweight HTTP Server?

    The Java Lightweight HTTP Server is an open-source implementation of an
    HTTP Server (a.k.a. web server). It is lightweight, i.e. small and
    efficient, yet provides various useful features commonly found in
    heavier HTTP servers. It can be used both as a standalone web server,
    and as an easily embedded server for integration into existing
    applications. This is commonly used to provide a convenient GUI
    (Graphical User Interface) which can be viewed across the network in
    a cross-platform manner from any computer with a web browser (just
    about all of them).
        
    This server is 'conditionally compliant' with RFC 2616 ("Hypertext
    Transfer Protocol -- HTTP/1.1"), which means it supports all functionality
    required by the RFC, as well as some of the optional functionality.
    Among the features are virtual hosts, partial content (i.e. download
    continuation), file-based serving, automatic directory index generation,
    GET/HEAD/POST/OPTIONS/TRACE method support, multiple contexts per host,
    file upload support and more.


2. How do I use the Java Lightweight HTTP Server?

    This server is intentionally written as a single source file, in order
    to make it as easy as possible to integrate into any existing project -
    by simply adding this single file to the project sources. It can also
    be used like any other library, by including the jar file in the
    application classpath.
    
    See the javadocs in the source file for the full API details. Examining
    the source code of the main method can be a good starting point for
    understanding how to embed the server in your application, as well.

    The server can also be run as a standalone application from the command
    line ('java -jar jlhttp.jar'), to serve files under a specified directory
    with no need for additional configuration.


3. License

    The Java Lightweight HTTP Server is provided under the GNU General
    Public License agreement. Please read the full license agreement
    in the included LICENSE.txt file.

    For non-GPL commercial licensing please contact the address below.


4. Contact

    Please write to support@freeutils.net with any bugs, suggestions, fixes,
    contributions, or just to drop a good word and let me know you've found
    this server useful and you'd like it to keep being maintained.

    Updates and additional info can be found at
    http://www.freeutils.net/source/jlhttp/
