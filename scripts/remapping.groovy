mappingfile = 'obf2cb.srg'

def readMapping(filename) {
    reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(scriptFolder, filename))))
    
    line = 'a'
    
    while (line != null) {
	line = reader.readLine()
	
	if(line == null)
	{
	    continue
	}
	
	pieces = line.trim().split(' ')
	
	switch(pieces[0]) {
	    case 'PK:':
	    	if(pieces.length != 3) {
		    logger.warning('Invalid mapping line: ' + line)
		    continue
		}
		    
		remapper.remapPackage(pieces[1], pieces[2])
	    	break
	    case 'CL:':
        	if(pieces.length != 3) {
		    logger.warning('Invalid mapping line: ' + line)
		    continue
        	}
		
		classPieces = pieces[1].split('/')
		remapPieces = pieces[2].split('/')
		
		if(classPieces.length == 1 && remapPieces.length == 1) {
		    remapper.remapClass('', pieces[1], pieces[2])
		}
		else if(classPieces.length == 1 && remapPieces.length > 1) {
		    remapper.remapClass('', pieces[1], remapPieces[remapPieces.length - 1])
		}
		else {
		    remapper.remapClass(pieces[1].replace('/' + classPieces[classPieces.length - 1], ''), classPieces[classPieces.length - 1], remapPieces[remapPieces.length - 1])
		}
	    	break
	    case 'MD:':
        	if(pieces.length != 5) {
		    logger.warning('Invalid mapping line: ' + line)
		    continue
        	}
		
		classPieces = pieces[1].split('/')
		remapPieces = pieces[3].split('/')
		
		if(classPieces.length == 1 && remapPieces.length == 1) {
		    remapper.remapMethod('', pieces[2], pieces[1], pieces[3])
		}
		else if(classPieces.length == 1 && remapPieces.length > 1) {
		    remapper.remapMethod('', pieces[2], pieces[1], remapPieces[remapPieces.length - 1])
		}
		else {
		    remapper.remapMethod(pieces[1].replace('/' + classPieces[classPieces.length - 1], ''), pieces[2], classPieces[classPieces.length - 1], remapPieces[remapPieces.length - 1])
		}
		
	    	break
	    case 'FD:':
        	if(pieces.length != 3) {
		    logger.warning('Invalid mapping line: ' + line)
		    continue
        	}
		
		classPieces = pieces[1].split('/')
		remapPieces = pieces[2].split('/')
		
		if(classPieces.length == 1 && remapPieces.length == 1) {
		    remapper.remapField('', pieces[1], pieces[2])
		}
		else if(classPieces.length == 1 && remapPieces.length > 1) {
		    remapper.remapField('', pieces[1], remapPieces[remapPieces.length - 1])
		}
		else {
		    remapper.remapField(pieces[1].replace('/' + classPieces[classPieces.length - 1], ''), classPieces[classPieces.length - 1], remapPieces[remapPieces.length - 1])
		}
	    	break
	}
    }
}

readMapping(mappingfile)