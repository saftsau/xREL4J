xREL4J
======

Introduction
------------

Java implementation for the usage of the xREL API v2. Method and parameter names are based on the xREL API with "_" replaced by camel case. Order of methods and parameters are based on the xREL API documentation.

It is **STRONGLY** encouraged to read the xREL API documentation!
https://www.xrel.to/wiki/1681/API.html

Requirements
------------

In order to use xREL4J you need a couple of prerequisites:

- Minimum required Java version is Java SE 6.

- A JSON library, e.g. [JSON-java](https://github.com/stleary/JSON-java)
  or any other JSON library. You might need to modify some code if you do
  use a different JSON library.

Setup
-----

- Get yourself a snapshot from this repository:

    git clone https://github.com/saftsau/xREL4J.git
    
- Get your favored JSON library.

- Add xREL4J and your chosen JSON library to your project.

- That's it!

Usage
-----

Following are some examples for really basic usage of xREL4J. As a complete Javadoc is included and there is also extensive documentation of the xREL API, this should just be a starting point. It is **strongly** advised to also read the xREL API documentation.

Basic usage without any authentication:
```java
Xrel xrel = new Xrel(null, null, null, null, null);
Release release = xrel.getReleaseInfoId("f638d1cfec8d");
System.out.println(release.getDirname());
```

Basic usage with authentication and scopes:
```java
Xrel xrel = new Xrel(CLIENT_ID, CLIENT_SECRET, REDIRECT_URI, null, new String[] { "viewnfo", "addproof" });
Release release = xrel.getReleaseInfoId("f638d1cfec8d");
BufferedImage bufferedImage = xrel.getNfoRelease(release, token);
```

Rate checking:
```java
System.out.println("X-RateLimit-Limit: " + xrel.getXRateLimitLimit());
System.out.println("X-RateLimit-Remaining: " + xrel.getXRateLimitRemaining());
System.out.println("X-RateLimit-Reset: " + xrel.getXRateLimitReset());
```

xREL API Errors (and connection problems) are returned as XrelException. You can get the information returned by the xREL API from these exceptions:
```java
catch (XrelException e) {
	System.out.println(e.getError());
	System.out.println(e.getErrorType());
	System.out.println(e.getErrorDescription());
	System.out.println(e.getResponseCode());
} 
```

The parsing of JSON Web Tokens is **not** included. If you want to have that feature in your application as well, you have to include a JWT library you want to use and decode the token for yourself:
```java
Xrel xrel = new Xrel(CLIENT_ID, CLIENT_SECRET, REDIRECT_URI, null, new String[] { "viewnfo", "addproof" });
Token token = xrel.postOauth2Token("authorization_code", code, null); // code = the code you got from the xREL OAuth
String accessToken = token.getAccessToken());
... // Decode the JWT
```

Reporting bugs
--------------

To report a bug:
- Use the "issues" system on GitHub
- Send me a message on xREL ("saftsau")
- Post in the xREL "Developer Forum" section (https://www.xrel.to/forum-topics.html?id=53)