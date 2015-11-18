import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.String;
import java.util.Arrays;
import java.util.LinkedList;

public class CspGenerator{
	private String gameName;

	private String[] inputKey;
	private String[] avaInitLoc;
	private String controlType;
	private Integer[] aniStep;
	private Integer[] aniTrace;
	private String[] levelScore;
	private Integer gameTick;
	private Integer lifeNum;
	private String missType;
	private String missTrigger;
	private Integer[] missAvaLoc;
	private Integer[] missAniNum;
	private Integer[] missAniSeq;
	private String scoreType;
	private Integer scoreLoc;

	private String cspStr;

	public CspGenerator(String gameName){
		this.gameName = gameName;
		cspStr = "";
	}

	//function to parse inputKey string into inputKey
	//to do: check validation of input key
	public void setInput(String inputKey){
		this.inputKey = inputKey.split(",");
	}

	//function to parse initLocStr into avaInitLoc
	//to do: check validation of avatar location value(can only be 1 or 0)
	public void setAvaLoc(String initLocStr){
		this.avaInitLoc = initLocStr.split(",");
	}

	//function to parse type string
	//check validation of input key array and avatar location length
	public void setContorlType(String type){
		this.controlType = type;
		if(type == "1-1 location"){
			if( inputKey.length != avaInitLoc.length ){
				throw new RuntimeException("Error: number of input is different form that of avatar location");
			}
		}
		else if( type == "1-1 step"){
			if( inputKey.length != 2 ){
				throw new RuntimeException("Error: unsupported number of input for 1-1 step, should be 2");
			}
			if( avaInitLoc.length < 2){
				throw new RuntimeException("Error: unsupported number of avatar location for 1-1 step, at least 2");
			}
		}
	}

	//function to parse animation string
	//to do: check validation of steps(should be >0) and trace value(should only be 1 or 0)
	public void setAnimation(String aniStr){
		String[] aniArr = aniStr.split(";");
		this.aniStep = new Integer[aniArr.length];
		this.aniTrace = new Integer[aniArr.length];
		for(int i = 0; i != aniArr.length; i++){
			String[] aniPair = aniArr[i].split(",");
			this.aniStep[i] = Integer.parseInt(aniPair[0]);
			this.aniTrace[i] = Integer.parseInt(aniPair[1]);
		}
	}

	//function to parse levelScore string
	//to do: check validation of level score array, should >0 and increment with level number
	public void setLevel(String levelScoreStr){
		this.levelScore = levelScoreStr.split(",");
	}

	//function to parse timeTick string
	//check whether it is greater than 0
	public void setGameTick(String timeTickStr){
		this.gameTick = Integer.parseInt(timeTickStr);
		if(this.gameTick <= levelScore.length){
			throw new RuntimeException("Error: invalid number of gameTick, should be greater than level number");
		}
	}

	//function to parse lifeNum string
	//check whether it is greater than 0
	public void setLife(String lifeNum){
		this.lifeNum = Integer.parseInt(lifeNum);
		if(this.gameTick <= 0){
			throw new RuntimeException("Error: invalid number of lifeNum, should be greater than 0");
		}
	}

	//function to parse missStr 
	//to do: check compatibility of missMap, avatarLoc array and aniStep array
	public void setMiss(String missType, String missTrigger, String missStr){
		String[] missArr = missStr.split(";");
		missAvaLoc = new Integer[missArr.length];
		missAniNum = new Integer[missArr.length];
		missAniSeq = new Integer[missArr.length];
		this.missType = missType;
		this.missTrigger = missTrigger;
		for(int i = 0; i!= missArr.length; i++){
			String[] missParas = missArr[i].split(",");
			missAvaLoc[i] = Integer.parseInt(missParas[0]);
			missAniNum[i] = Integer.parseInt(missParas[1]);
			missAniSeq[i] = Integer.parseInt(missParas[2]);
			if(missAvaLoc[i] >= avaInitLoc.length || missAvaLoc[i] < 0){
				throw new RuntimeException("Error: invalid number of miss avatar location, should be less than number of avatar location and greater than 0");
			}
			if(missAniNum[i] >= aniStep.length || missAniNum[i] < 0){
				throw new RuntimeException("Error: invalid number of miss animation number, should be less than number of animation and greater than 0");
			}
			if(missAniSeq[i] >= aniStep[missAniNum[i]] || missAniSeq[i] < 0){
				throw new RuntimeException("Error: invalid number of miss animation step number, should be less than number of animation step and greater than 0");
			}
		}
	}

	//functio to parse score type
	//to do: allow multiple avatar location for score
	public void setScore(String scoreType, String scoreStr){
		this.scoreType = scoreType;
		if(this.scoreType == "Avatar Location"){
			this.scoreLoc = Integer.parseInt(scoreStr);
		}
	}

	private void writeVars(){
		//Game general variables
		cspStr += "var Input : [ 0, "+(this.controlType=="1-1 step"?this.inputKey.length:this.inputKey.length-1)+" ];\n";
		cspStr += "var Life : [ 0, "+this.lifeNum+" ];\n";
		cspStr += "var Score : [ 0, 1 ];\n";
		cspStr += "var Gametick : [ 0, "+(this.gameTick-1)+" ];\n";
		cspStr += "var GameOver : [ 0, 1 ];\n";
		cspStr += "var Level : [ 0, "+this.levelScore.length+" ];\n";
		cspStr += "var Random : [ 0, 1 ];\n";


		cspStr += "\n";
		//Avatar Location variables
		cspStr += "var Avatar : [ 0, "+(avaInitLoc.length-1)+" ];\n";
		for(int i=0;i<avaInitLoc.length;i++){
			cspStr += "var Ava"+i+" : [ 0, 1 ];\n";
		}
		cspStr += "\n";

		//Game animation variables
		//for trace or not, different mechnism is applied
		for(int i=0;i<aniStep.length;i++){
			if(aniTrace[i] == 0){
				cspStr += "var Ani"+i+" : [ 0 , "+aniStep[i]+" ];\n";
			}
			else{
				cspStr += "var Ani"+i+" : [ 0, "+(aniStep[i]*2-1)+" ];\n";
			}
			for(int j=0;j<aniStep[i];j++){
				cspStr += "var Ani"+i+"Seq"+j+" : [ 0, 1 ];\n";
			}
		}
		cspStr += "\n";

		//Miss location variables
		cspStr += "var Miss : [ 0, 1 ];\n";
		for(int i=0; i!=missAvaLoc.length;i++){
			cspStr += "var MissLoc"+i+" : [ 0, 1 ];\n";
		}
		cspStr += "\n";
		
	}

	private void writeInitCondition(){
		cspStr += "\n";

		//Game General variable
		cspStr += "first Input == 0;\n";
		cspStr += "first Life == "+this.lifeNum+";\n";
		cspStr += "first Score == 0;\n";
		cspStr += "first Gametick == "+(this.gameTick-1)+";\n";
		cspStr += "first GameOver == 0;\n";
		cspStr += "first Level == 0;\n";

		cspStr += "\n";
		//Avatar Location variables
		cspStr += "first Avatar == 0;\n";

		//Game animation variables
		for(int i=0; i!=aniStep.length; i++){
			cspStr += "first Ani"+i+" == 0;\n";
		}

		//Game miss variables
		for(int i=0; i!=missAvaLoc.length;i++){
			cspStr += "first MissLoc"+i+" == 0;\n";
		}
	}

	private void writeConstraints(){
		cspStr += "\n";

		//Game General constraint
		cspStr += "GameOver == Life eq 0;\n";
		cspStr += "next Gametick == (Gametick+1)%("+this.gameTick+"-Level);\n";
		cspStr += "next Life == if Life eq 0 then 0 else if (Miss eq 1) and (Gametick eq 0) then (Life-1) else Life;\n";

		//Level setting
		//todo: setting different level
		cspStr += "Level == 0;\n";
		cspStr += "\n";

		//Avatar Location constraint
		for( int i=0; i != this.avaInitLoc.length; i++ ){
			cspStr += "Ava"+i+" == Avatar eq "+i+";\n";
		}
		cspStr += "\n";

		//Animation constraint
		for(int i=0; i!= aniTrace.length; i++){
			if(aniTrace[i] == 0){
				for(int j=0; j!= aniStep[i]; j++){
					cspStr += "Ani"+i+"Seq"+j+" == ( Ani"+i+" eq "+(j+1)+" );\n";
				}
			}
			if(aniTrace[i] == 1){
				for(int j=0; j != aniStep[i]; j++){
					cspStr += "Ani"+i+"Seq"+j+" == (Ani"+i+" ge "+(j+1)+") and (Ani"+i+" le "+(aniStep[i]-1-j)+");\n";
				}
			}
		}
		cspStr += "\n";

		//miss constraint
		cspStr += "Miss == ";
		String[] missStr = new String[missAvaLoc.length];
		for(int i=0; i!= missAvaLoc.length; i++){
			missStr[i] = "MissLoc"+i;
		}
		cspStr += String.join(" or ", missStr)+";\n";
		if(this.missType == "positive"){
			for(int i=0; i!= missAvaLoc.length; i++){
				cspStr += "MissLoc"+i+" == (Gametick eq 0) and (Ava"+missAvaLoc[i]+" eq 1) and (Ani"+missAniNum[i]+"Seq"+missAniSeq[i]+" eq 1);\n";
			}
		}
		if(this.missType == "negative"){
			for(int i=0; i!= missAvaLoc.length; i++){
				cspStr += "MissLoc"+i+" == (Gametick eq 0) and (Ava"+missAvaLoc[i]+" eq 0) and (Ani"+missAniNum[i]+"Seq"+missAniSeq[i]+" eq 1);\n";
			}
			for(int i=0; i!= aniStep.length; i++){
				if ( Arrays.asList(missAniNum).contains(i)){
					for(int j=0; j!= aniStep[i]; j++){
						cspStr += "Ani"+i+"Seq"+j;
						if(j != aniStep[i]-1){
							cspStr += " + ";
						}
						else{
							cspStr += " <= 1;\n";
						}
					}
				}
			}
		}
		cspStr += "\n";

		//setting avatar variable
		cspStr += "next Avatar == if GameOver then Avatar else ";
		if(this.missTrigger == "Reset Avatar"){
			cspStr += "if Miss eq 1 then 0 else ";
		}
		if(this.controlType == "1-1 location"){
			cspStr += "Input;\n";
		}
		else if( this.controlType == "1-1 step"){
			//0 for no input and 1 input for left and 2 input for right
			int lastAva = this.avaInitLoc.length-1;
			cspStr += "if Input eq 0 then Avatar else if Input eq 1 and Avatar eq 0 then 0 else if Input eq 2 and Avatar eq "+lastAva+" then "+lastAva+" else if Input eq 1 then (Avatar - 1) else (Avatar + 1);\n";
		}
		

		//setting animation variable
		for(int i=0; i!= aniStep.length;i++){
			cspStr += "next Ani"+i+" == if Gametick ne 0 then Ani"+i+" else ";
			if(this.missTrigger == "Reset Animation" && Arrays.asList(missAniNum).contains(i)){
				LinkedList<String> missLocStr = new LinkedList<String>();
				for( int j=0; j!= missAniNum.length; j++){
					if(i == missAniNum[j]){
						missLocStr.add("MissLoc"+j);
					}
				}
				cspStr += "if ("+String.join(" or ", missLocStr) + ") or GameOver then 0 else ";
			}

			if(aniTrace[i] == 0){
				cspStr += "if Ani"+i+" eq 0 then Random else (Ani"+i+"+1)%"+(aniStep[i]+1)+";\n";
			}
			else{
				cspStr += "if Ani"+i+" eq 0 then Random else (Ani"+i+"+1)%"+(aniStep[i]*2)+";\n";
			}
		}
		cspStr += "\n";

		//score constraint
		//bug: for avatar location, if avatar location not change then score would always be 1
		//to do: sequences of avatar location for scoring
		if(this.scoreType == "Avatar Location"){
			cspStr += "Score == (Ava"+this.scoreLoc+" eq 1) and (Gametick eq 0);\n";
		}
		if(this.scoreType == "Not Miss"){
			cspStr += "Score == if Gametick ne 0 then 0 else ( ";
			if(this.missType == "positive"){
				for(int i=0; i!= missAvaLoc.length; i++){
					cspStr += "( Ava"+missAvaLoc[i]+" eq 0 and Ani"+missAniNum[i]+"Seq"+missAniSeq[i]+" eq 1 )";
					if(i != missAvaLoc.length - 1){
						cspStr += " or ";
					}
					else{
						cspStr += ";\n";
					}
				}	
			}
			if(this.missType == "negative"){
				for(int i=0; i!= missAvaLoc.length; i++){
					cspStr += "( Ava"+missAvaLoc[i]+" eq 1 and Ani"+missAniNum[i]+"Seq"+missAniSeq[i]+" eq 1 )";
					if(i != missAvaLoc.length - 1){
						cspStr += " or ";
					}
					else{
						cspStr += " );\n";
					}
				}
			}
		}
		cspStr += "\n";
	}


	public void generateCsp(){
		writeVars();
		writeInitCondition();
		writeConstraints();
		File exportFile = new File(this.gameName+".csp");
		BufferedWriter writer;
		try{
			writer = new BufferedWriter(new FileWriter(exportFile));
			writer.write(cspStr);
			writer.flush();
			writer.close();
		} catch (IOException e){
			e.printStackTrace();
		}
	}
}











