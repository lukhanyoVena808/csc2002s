#Makefile 
#Lukhanyo Vena

JAVAC=/usr/bin/javac
.SUFFIXES: .java .class

SRCDIR=src
BINDIR=bin

$(BINDIR)/%.class:$(SRCDIR)/%.java
	$(JAVAC) -d $(BINDIR)/ -cp $(BINDIR) $<

CLASSES=MeanFilterSerial.class MeanFilterParallel.class \
		MedianFilterSerial.class MedianFilterParallel.class \
		StopWatch.class \

CLASS_FILES =$(CLASSES:%.class=$(BINDIR)/%.class)

default: $(CLASS_FILES)

clean:
	rm $(BINDIR)/*.class

runMnSerial: $(CLASS_FILES)
	java -cp $(BINDIR) MeanFilterSerial
