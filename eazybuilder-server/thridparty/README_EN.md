# Third party plugins

EazyBuilder integrates plugins such as jenkins, sonarqube, etc. This directory contains the Dockerfile for making plugin images. Recreating the image using the Dockerfile is not necessary. You can configure the existing plugin access address directly in the EazyBuilder configuration file.

# directory description

## arm64 support
- The platform is compatible with arm64, and each plug-in directory contains the arm64 directory. It should be noted that arm64 is still in the test, and the related functions have not been fully tested;

##x86 support
- The platform uses the image of the x86 architecture by default.

## Notice
- The access address, password and other sensitive information contained in the file have been specially processed. Before re-creating the image, the access address and password should be set to real and effective information.