%% Calculer la différence
date_origine=  1589238000;
date_calculer = 1588976154 ; 
res=date_origine-date_calculer;
if(abs(res)>604800 && abs(res)<1209600)
    disp('4');
else
    if(abs(res)>1209600)
        disp('0');
    else
        disp('10');
    end
end