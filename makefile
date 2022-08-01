#Makefile 
#Lukhanyo Vena

JAVAC=/usr/bin/javac
.SUFFIXES: .java .class

SRCDIR=src
BINDIR=bin

$(BINDIR)/%.class:$(SRCDIR)/%.java
	$(JAVAC) -d $()/ -cp $(BINDIR) $<

CLASSES=

CLASS_FILES =$(CLASSES:%.class=$(BINDIR)/%.class)

default: $(CLASS_FILES)

clean:
	rm $(BINDIR)/*.class

runThread: $(CLASS_FILES)
	java -cp $(BINDIR) 
