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

CLASS_FILES =$(CLASSES:%.class=$(BINDIR)/%.class)

default: $(CLASS_FILES)

clean:
	rm $(BINDIR)/*.class

mean: $(CLASS_FILES)
	java -cp $(BINDIR) MeanFilterSerial $(in) $(out) $(size)

median: $(CLASS_FILES)
	java -cp $(BINDIR) MedianFilterSerial $(in) $(out) $(size)

meanp: $(CLASS_FILES)
	java -cp $(BINDIR) MeanFilterParallel $(in) $(out) $(size)

medianp: $(CLASS_FILES)
	java -cp $(BINDIR) MedianFilterParallel $(in) $(out) $(size)
