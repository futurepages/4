update global_unidadeorganizacional set descricao = 'Cartório Único de Capitão Gervásio Oliveira (Termo)', caminho = 'Comarcas > São João do Piauí > Cartório Único de Capitão Gervásio Oliveira (Termo)' where id = 1529;
update global_unidadeorganizacional set descricao = 'Cartório Único de Lagoa do Barro do Piauí (Termo)', caminho = 'Comarcas > São João do Piauí > Cartório Único de Lagoa do Barro do Piauí (Termo)' where id = 1531;

drop procedure if exists atualizaPrediosCartorios;

delimiter //

create procedure atualizaPrediosCartorios() 
begin
	declare done int default false;
	declare posicao int default 0;
	declare qtde_cidade int default 0;
	declare descricao_comarca varchar(255);
	declare descricao_orgao_termo varchar(255);
	declare descricao_orgao varchar(255);	
	declare nome_cidade varchar(255);	
	declare nome_predio varchar(255);		
	declare id_orgao bigint(20);
	declare id_cidade bigint(20);
	declare id_endereco bigint(20);
	
	declare cur1 cursor for SELECT un.id, un.descricao
							FROM global_unidadeorganizacional un
							WHERE un.descricao LIKE  '%termo%'
							ORDER BY un.descricao;
	declare continue handler for sqlstate '02000' set done = 1;
	open cur1;
	
	repeat 
		fetch cur1 into id_orgao, descricao_comarca;			
		if not done then 		
			select substring_index(descricao_comarca, '(', 1) into descricao_orgao_termo;
			-- if necessario apenas para banco instalado --------------------------------
			-- select instr(descricao_orgao_termo,'\'') into posicao;			
			-- if posicao = 0 then				
				select trim(substring(descricao_orgao_termo,18)) into descricao_orgao; 								
			-- else
				-- select replace(trim(substring(descricao_orgao_termo,18)),'\'','`') into descricao_orgao; 								
			-- end if;
			select count(*) into qtde_cidade from global_cidade where nome = descricao_orgao and estado_sigla = 'PI';						
			if qtde_cidade > 0 then
				select id, nome into id_cidade, nome_cidade from global_cidade where nome = descricao_orgao and estado_sigla = 'PI';				
				select id into id_endereco from global_endereco where cidade_id = id_cidade limit 1;			
				insert into `intranet`.`global_predio` (`endereco_id`) values (id_endereco);
				set nome_predio = convert(concat('Não Cadastrado #',last_insert_id()) using utf8);
				update `intranet`.`global_predio` set nome = nome_predio, nomeBusca = concat(nome_predio,' - ',nome_cidade,' - PI') where id = last_insert_id();
				update `intranet`.`global_orgao` set predioSede_id = last_insert_id() where id = id_orgao;
				update `intranet`.`global_unidadeorganizacional` set descricao = descricao_orgao_termo where id = id_orgao;
			end if;
		end if;
	until done	
	end repeat;	
	commit;
	
	close cur1;
end //

delimiter ;

call atualizaPrediosCartorios();

update global_unidadeorganizacional set caminho = trim(substring_index(caminho, '(Termo)', 1)) where instr(caminho, '(Termo)') > 0;