/*
 *
 *  * Copyright 2017 Carlosthe19916, Inc. and/or its affiliates
 *  * and other contributors as indicated by the @author tags.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  * http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package io.github.carlosthe19916.webservices.models;

public enum InternetMediaType {

    GP3(".3gp", "video/3gpp"),
    A(".a", "application/octet-stream"),
    AI(".ai", "application/postscript"),
    AIF(".aif", "audio/x-aiff"),
    AIFF(".aiff", "audio/x-aiff"),
    ASC(".asc", "application/pgp-signature"),
    ASF(".asf", "video/x-ms-asf"),
    ASM(".asm", "text/x-asm"),
    ASX(".asx", "video/x-ms-asf"),
    ATOM(".atom", "application/atom+xml"),
    AU(".au", "audio/basic"),
    AVI(".avi", "video/x-msvideo"),
    BAT(".bat", "application/x-msdownload"),
    BIN(".bin", "application/octet-stream"),
    BMP(".bmp", "image/bmp"),
    BZ2(".bz2", "application/x-bzip2"),
    C(".c", "text/x-c"),
    CAB(".cab", "application/vnd.ms-cab-compressed"),
    CC(".cc", "text/x-c"),
    CHM(".chm", "application/vnd.ms-htmlhelp"),
    CLASS(".class", "application/octet-stream"),
    COM(".com", "application/x-msdownload"),
    CONF(".conf", "text/plain"),
    CPP(".cpp", "text/x-c"),
    CRT(".crt", "application/x-x509-ca-cert"),
    CSS(".css", "text/css"),
    CSV(".csv", "text/csv"),
    CXX(".cxx", "text/x-c"),
    DEB(".deb", "application/x-debian-package"),
    DER(".der", "application/x-x509-ca-cert"),
    DIFF(".diff", "text/x-diff"),
    DJV(".djv", "image/vnd.djvu"),
    DJVU(".djvu", "image/vnd.djvu"),
    DLL(".dll", "application/x-msdownload"),
    DMG(".dmg", "application/octet-stream"),
    DOC(".doc", "application/msword"),
    DOT(".dot", "application/msword"),
    DTD(".dtd", "application/xml-dtd"),
    DVI(".dvi", "application/x-dvi"),
    EAR(".ear", "application/java-archive"),
    EML(".eml", "message/rfc822"),
    EPS(".eps", "application/postscript"),
    EXE(".exe", "application/x-msdownload"),
    F(".f", "text/x-fortran"),
    F77(".f77", "text/x-fortran"),
    F990(".f90", "text/x-fortran"),
    FLV(".flv", "video/x-flv"), FOR(".for", "text/x-fortran"),
    GEM(".gem", "application/octet-stream"),
    GEMSPEC(".gemspec", "text/x-script.ruby"),
    GIF(".gif", "image/gif"), GZ(".gz", "application/x-gzip"),
    H(".h", "text/x-c"), HH(".hh", "text/x-c"),
    HTM(".htm", "text/html"),
    HTML(".html", "text/html"),
    ICO(".ico", "image/vnd.microsoft.icon"),
    ICS(".ics", "text/calendar"), IFB(".ifb", "text/calendar"),
    ISO(".iso", "application/octet-stream"),
    JAR(".jar", "application/java-archive"),
    JAVA(".java", "text/x-java-source"),
    JNPL(".jnlp", "application/x-java-jnlp-file"),
    JPEG(".jpeg", "image/jpeg"),
    JPG(".jpg", "image/jpeg"), JS(".js", "application/javascript"),
    JSON(".json", "application/json"),
    LOG(".log", "text/plain"),
    M3U(".m3u", "audio/x-mpegurl"),
    M4V(".m4v", "video/mp4"),
    MAN(".man", "text/troff"),
    MATHML(".mathml", "application/mathml+xml"),
    MBOX(".mbox", "application/mbox"),
    MDOC(".mdoc", "text/troff"),
    ME(".me", "text/troff"),
    MID(".mid", "audio/midi"),
    MIDI(".midi", "audio/midi"),
    MIME(".mime", "message/rfc822"),
    MML(".mml", "application/mathml+xml"),
    MNG(".mng", "video/x-mng"),
    MOV(".mov", "video/quicktime"),
    MP3(".mp3", "audio/mpeg"),
    MP4(".mp4", "video/mp4"),
    MP4V(".mp4v", "video/mp4"),
    MPEG(".mpeg", "video/mpeg"),
    MPG(".mpg", "video/mpeg"),
    MS(".ms", "text/troff"),
    MSI(".msi", "application/x-msdownload"),
    ODP(".odp", "application/vnd.oasis.opendocument.presentation"),
    ODS(".ods", "application/vnd.oasis.opendocument.spreadsheet"),
    ODT(".odt", "application/vnd.oasis.opendocument.text"),
    OGG(".ogg", "application/ogg"),
    P(".p", "text/x-pascal"),
    PAS(".pas", "text/x-pascal"),
    PBM(".pbm", "image/x-portable-bitmap"),
    PDF(".pdf", "application/pdf"),
    PEM(".pem", "application/x-x509-ca-cert"),
    PGM(".pgm", "image/x-portable-graymap"),
    PGP(".pgp", "application/pgp-encrypted"),
    PKG(".pkg", "application/octet-stream"),
    PL(".pl", "text/x-script.perl"),
    PM(".pm", "text/x-script.perl-module"),
    PNG(".png", "image/png"),
    PNM(".pnm", "image/x-portable-anymap"),
    PPM(".ppm", "image/x-portable-pixmap"),
    PPS(".pps", "application/vnd.ms-powerpoint"),
    PPT(".ppt", "application/vnd.ms-powerpoint"),
    PS(".ps", "application/postscript"),
    PSD(".psd", "image/vnd.adobe.photoshop"),
    PY(".py", "text/x-script.python"),
    QT(".qt", "video/quicktime"),
    RA(".ra", "audio/x-pn-realaudio"),
    RAKE(".rake", "text/x-script.ruby"),
    RAM(".ram", "audio/x-pn-realaudio"),
    RAR(".rar", "application/x-rar-compressed"),
    RB(".rb", "text/x-script.ruby"),
    RDF(".rdf", "application/rdf+xml"),
    ROFF(".roff", "text/troff"),
    RPM(".rpm", "application/x-redhat-package-manager"),
    RSS(".rss", "application/rss+xml"),
    RTF(".rtf", "application/rtf"),
    RU(".ru", "text/x-script.ruby"),
    S(".s", "text/x-asm"),
    SGM(".sgm", "text/sgml"),
    SGML(".sgml", "text/sgml"),
    SH(".sh", "application/x-sh"),
    SIG(".sig", "application/pgp-signature"),
    SND(".snd", "audio/basic"),
    SO(".so", "application/octet-stream"),
    SVG(".svg", "image/svg+xml"),
    SVGZ(".svgz", "image/svg+xml"),
    SWF(".swf", "application/x-shockwave-flash"),
    T(".t", "text/troff"),
    TAR(".tar", "application/x-tar"),
    TBZ(".tbz", "application/x-bzip-compressed-tar"),
    RCL(".tcl", "application/x-tcl"),
    ATEXT(".tex", "application/x-tex"),
    TEXI(".texi", "application/x-texinfo"),
    TEXINFO(".texinfo", "application/x-texinfo"),
    TEXT(".text", "text/plain"),
    TIF(".tif", "image/tiff"),
    TIFF(".tiff", "image/tiff"),
    TORRENT(".torrent", "application/x-bittorrent"),
    TR(".tr", "text/troff"),
    TEXTP(".txt", "text/plain"),
    VCF(".vcf", "text/x-vcard"),
    VCS(".vcs", "text/x-vcalendar"),
    VRML(".vrml", "model/vrml"),
    WAR(".war", "application/java-archive"),
    WAV(".wav", "audio/x-wav"),
    WMA(".wma", "audio/x-ms-wma"),
    WMV(".wmv", "video/x-ms-wmv"),
    WMX(".wmx", "video/x-ms-wmx"),
    WRL(".wrl", "model/vrml"),
    WSDL(".wsdl", "application/wsdl+xml"),
    XBM(".xbm", "image/x-xbitmap"),
    XHTML(".xhtml", "application/xhtml+xml"),
    XLS(".xls", "application/vnd.ms-excel"),
    XML(".xml", "application/xml"),
    XPM(".xpm", "image/x-xpixmap"),
    XSL(".xsl", "application/xml"),
    XSLT(".xslt", "application/xslt+xml"),
    YAML(".yaml", "text/yaml"),
    YML(".yml", "text/yaml"),
    ZIP(".zip", "application/zip");

    private final String extension;
    private final String mimeType;

    InternetMediaType(String extension, String mimeType) {
        this.extension = extension;
        this.mimeType = mimeType;
    }

    public String getExtension() {
        return extension;
    }

    public String getMimeType() {
        return mimeType;
    }

}
