function [results, tr] = rnaTrain()
    FirstLayerNeurons = [];
    SecondLayerNeurons = [];
    MedianEntr = [];
    StdEntr = [];
    MedianVal = [];
    StdVal = [];
    MedianTest = [];
    StdTest = [];
    entradas = xlsread('Redes Neuronales Min.xlsx', 'Entradas RNA')';
    salidasDeseadas = xlsread('Redes Neuronales Min.xlsx', 'Salidas RNA')';
    for i=128:128
        %for j=0:0
            [mpreEntr, mpreVal, mpreTest, dpreEntr, dpreVal, dpreTest, tr] = rnaTrainArchitecture([i], entradas, salidasDeseadas);
            FirstLayerNeurons(end + 1) = i;
            SecondLayerNeurons(end + 1) = 0;
            MedianEntr(end + 1) = mpreEntr;
            StdEntr(end + 1) = dpreEntr;
            MedianVal(end + 1) = mpreVal;
            StdVal(end + 1) = dpreVal;
            MedianTest(end + 1) = mpreTest;
            StdTest(end + 1) = dpreTest;
        %end
    end
    
    results = [FirstLayerNeurons; SecondLayerNeurons; MedianEntr; StdEntr; MedianVal; StdVal; MedianTest; StdTest];
end

function [mpreEntr, mpreVal, mpreTest, dpreEntr, dpreVal, dpreTest, tr] = rnaTrainArchitecture(architecture, entradas, salidasDeseadas)
    
    entrenamiento = [];
    validacion = [];
    test = [];
    for i=1:1
        rna = patternnet(architecture);
        rna.trainParam.max_fail=8;
        [rna, tr] = train(rna, entradas, salidasDeseadas);
        
        letterOutputs = sim(rna, entradas);
        
        entrenamiento(end+1) = 1-confusion(salidasDeseadas(:,tr.trainInd), letterOutputs(:,tr.trainInd));
        validacion(end+1) = 1-confusion(salidasDeseadas(:,tr.valInd), letterOutputs(:,tr.valInd));
        test(end+1) = 1-confusion(salidasDeseadas(:,tr.testInd), letterOutputs(:,tr.testInd));
    end
    
    mpreEntr = mean(entrenamiento);
    mpreVal = mean(validacion);
    mpreTest = mean(test);
    dpreEntr = std(entrenamiento);
    dpreVal = std(validacion);
    dpreTest = std(test);
end