package simulator.launcher;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import javax.swing.SwingUtilities;

/*
 * Examples of command-line parameters:
 * 
 *  -h
 *  -i resources/examples/ex4.4body.txt -s 100
 *  -i resources/examples/ex4.4body.txt -o resources/examples/ex4.4body.out -s 100
 *  -i resources/examples/ex4.4body.txt -o resources/examples/ex4.4body.out -s 100 -gl ftcg
 *  -i resources/examples/ex4.4body.txt -o resources/examples/ex4.4body.out -s 100 -gl nlug
 *
 */

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.json.JSONObject;

import simulator.control.Controller;
import simulator.factories.BasicBodyBuilder;
import simulator.factories.Builder;
import simulator.factories.BuilderBasedFactory;
import simulator.factories.Factory;
import simulator.factories.FallingtoCenterGravityBuilder;
import simulator.factories.MassLosingBodyBuilder;
import simulator.factories.NewtonUniversalGravitationBuilder;
import simulator.factories.NoGravityBuilder;
import simulator.model.Body;
import simulator.model.GravityLaws;
import simulator.model.PhysicsSimulator;
import simulator.view.MainWindow;

public class Main {

	// default values for some parameters
	//
	private final static Double _dtimeDefaultValue = 2500.0;
	private static final int _stepsDefaultValue = 150;
	private static final OutputStream _outputDefaultValue = System.out;
	private static final String _modeDefaultValue = "batch";

	// some attributes to stores values corresponding to command-line parameters
	//
	private static Double _dtime = null;
	private static String _inFile = null;
	private static String _outFile = null;
	private static String _mode = null;
	private static JSONObject _gravityLawsInfo = null;
	private static int _simSteps = 0;
	
	// factories
	private static Factory<Body> _bodyFactory;
	private static Factory<GravityLaws> _gravityLawsFactory;

	private static void init() {
		// initialize the bodies factory
		ArrayList<Builder<Body>> listabody = new ArrayList<>();
		listabody.add(new BasicBodyBuilder());
		listabody.add(new MassLosingBodyBuilder());
		_bodyFactory = new BuilderBasedFactory<Body>(listabody);
		// initialize the gravity laws factory
		ArrayList<Builder<GravityLaws>> listagrav = new ArrayList<>();
		listagrav.add(new NoGravityBuilder());
		listagrav.add(new FallingtoCenterGravityBuilder());
		listagrav.add(new NewtonUniversalGravitationBuilder());
		_gravityLawsFactory= new BuilderBasedFactory<GravityLaws>(listagrav);
	}

	private static void parseArgs(String[] args) {
		
		// define the valid command line options
		Options cmdLineOptions = buildOptions();

		// parse the command line as provided in args
		CommandLineParser parser = new DefaultParser();
		try {
			CommandLine line = parser.parse(cmdLineOptions, args);
			parseHelpOption(line, cmdLineOptions);
			parseInFileOption(line);
			parseDeltaTimeOption(line);
			parseGravityLawsOption(line);
			
			parseOutFileOption(line);
			parseStepsOption(line);
			paseExecuteModeOption(line);
			
			// if there are some remaining arguments, then something wrong is
			// provided in the command line!
			//
			String[] remaining = line.getArgs();
			if (remaining.length > 0) {
				String error = "Illegal arguments:";
				for (String o : remaining)
					error += (" " + o);
				throw new ParseException(error);
			}

		} catch (ParseException e) {
			System.err.println(e.getLocalizedMessage());
			System.exit(1);
		}

	}

	private static Options buildOptions() {
		Options cmdLineOptions = new Options();
		
		//output file
		cmdLineOptions.addOption(Option.builder("o").longOpt("output").hasArg().desc("Output file, where output is written."
				+ " Default value: the standard output.").build());
		
		//# steps
		cmdLineOptions.addOption(Option.builder("s").longOpt("steps").hasArg().desc("An integer representing the number "
				+ "of simulation steps. Default value: " + _stepsDefaultValue + ".").build());
		
		//mode
		cmdLineOptions.addOption(Option.builder("m").longOpt("mode").hasArg().desc(" Execution Mode. Possible values: ’batch’\n" + 
				"(Batch mode), ’gui’ (Graphical User Interface mode). Default value: " + _modeDefaultValue + ".").build());

		
		// help
		cmdLineOptions.addOption(Option.builder("h").longOpt("help").desc("Print this message.").build());

		// input file
		cmdLineOptions.addOption(Option.builder("i").longOpt("input").hasArg().desc("Bodies JSON input file.").build());

		// delta-time
		cmdLineOptions.addOption(Option.builder("dt").longOpt("delta-time").hasArg()
				.desc("A double representing actual time, in seconds, per simulation step. Default value: "
						+ _dtimeDefaultValue + ".")
				.build());

		// gravity laws -- there is a workaround to make it work even when
		// _gravityLawsFactory is null. 
		//
		String gravityLawsValues = "N/A";
		String defaultGravityLawsValue = "N/A";
		if (_gravityLawsFactory != null) {
			gravityLawsValues = "";
			for (JSONObject fe : _gravityLawsFactory.getInfo()) {
				if (gravityLawsValues.length() > 0) {
					gravityLawsValues = gravityLawsValues + ", ";
				}
				gravityLawsValues = gravityLawsValues + "'" + fe.getString("type") + "' (" + fe.getString("desc") + ")";
			}
			defaultGravityLawsValue = _gravityLawsFactory.getInfo().get(0).getString("type");
		}
		cmdLineOptions.addOption(Option.builder("gl").longOpt("gravity-laws").hasArg()
				.desc("Gravity laws to be used in the simulator. Possible values: " + gravityLawsValues
						+ ". Default value: '" + defaultGravityLawsValue + "'.")
				.build());

		return cmdLineOptions;
	}

	private static void parseHelpOption(CommandLine line, Options cmdLineOptions) {
		if (line.hasOption("h")) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp(Main.class.getCanonicalName(), cmdLineOptions, true);
			System.exit(0);
		}
	}

	private static void parseInFileOption(CommandLine line) throws ParseException {
		_inFile = line.getOptionValue("i");
	}

	private static void parseOutFileOption(CommandLine line) {
		//si hay fichero guarda su nombre, si no el string es null
		_outFile = line.getOptionValue("o");
	}
	
	private static void parseDeltaTimeOption(CommandLine line) throws ParseException {
		String dt = line.getOptionValue("dt", _dtimeDefaultValue.toString());
		try {
			_dtime = Double.parseDouble(dt);
			assert (_dtime > 0);
		} catch (Exception e) {
			throw new ParseException("Invalid delta-time value: " + dt);
		}
	}

	private static void parseGravityLawsOption(CommandLine line) throws ParseException {
		
		// this line is just a work around to make it work even when _gravityLawsFactory
		// is null, you can remove it when've defined _gravityLawsFactory
		
		/*if (_gravityLawsFactory == null)
			return;*/

		String gl = line.getOptionValue("gl");
		if (gl != null) {
			for (JSONObject fe : _gravityLawsFactory.getInfo()) {
				if (gl.equals(fe.getString("type"))) {
					_gravityLawsInfo = fe;
					break;
				}
			}
			if (_gravityLawsInfo == null) {
				throw new ParseException("Invalid gravity laws: " + gl);
			}
		} else {
			_gravityLawsInfo = _gravityLawsFactory.getInfo().get(0);
		}
	}

	private static void parseStepsOption(CommandLine line) throws ParseException {
		String steps = line.getOptionValue("s");
		if(steps!=null) {
			try {
				_simSteps = Integer.parseInt(steps);
				assert (_simSteps > 0);//asegura que el valor sea positivo estricto
			}
			catch(Exception e) {
				throw new ParseException("Invalid steps value: " + steps);
			}
		}
		else {
			_simSteps =_stepsDefaultValue;
		}
	}
	
	private static void paseExecuteModeOption(CommandLine line) {
		_mode = line.getOptionValue("m");
	}
	
	private static void startBatchMode() throws Exception {
		// create and connect components, then start the simulator
		//crear instancia del simulador y controller
		GravityLaws gravityLaws = _gravityLawsFactory.createInstance(_gravityLawsInfo);
		PhysicsSimulator sim = new PhysicsSimulator(gravityLaws, _dtime);
		Controller control = new Controller(sim,_bodyFactory, _gravityLawsFactory); 
		
		//crear fichero de entrada y salida
		if(_inFile==null) throw new Exception("An input file is required in batch mode.");
		InputStream is = new FileInputStream(new File (_inFile));
		OutputStream os;
		if(_outFile!=null) os = new FileOutputStream(new File(_outFile));
		else os = _outputDefaultValue;
		//cargar cuerpos y ejecutar
		control.loadBodies(is);
		control.run(_simSteps, os);		
	}
	
	private static void startGUIMode() throws InvocationTargetException, InterruptedException, FileNotFoundException {
		
		GravityLaws gravityLaws = _gravityLawsFactory.createInstance(_gravityLawsInfo);
		PhysicsSimulator sim = new PhysicsSimulator(gravityLaws, _dtime);
		Controller control = new Controller(sim,_bodyFactory, _gravityLawsFactory); 
		
		if(_inFile!=null) {
			InputStream is = new FileInputStream(new File (_inFile));
			control.loadBodies(is);
		}

		control.run(_simSteps);
		
		SwingUtilities.invokeAndWait(new Runnable() {
			@Override
			public void run() {
				new MainWindow(control);
			}
		});
		
	}

	private static void start(String[] args) throws Exception {
		parseArgs(args);
		
		if(_mode.equals("gui") || _mode.equals("GUI")) {
			startGUIMode();
		}
		else if(_mode.equals("batch") || _mode.equals("BATCH") || _mode==null){
			//si es batch o null 
			startBatchMode();
		}
		else {
			throw new Exception("Mode "+ _mode+" does not exit.");
		}
	}

	

	public static void main(String[] args) {
		try {
			init();
			start(args);
		} catch (Exception e) {
			System.err.println("Something went wrong ...");
			System.err.println();
			e.printStackTrace();
		}
	}
}
