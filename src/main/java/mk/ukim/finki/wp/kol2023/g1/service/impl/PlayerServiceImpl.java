package mk.ukim.finki.wp.kol2023.g1.service.impl;

import mk.ukim.finki.wp.kol2023.g1.model.Player;
import mk.ukim.finki.wp.kol2023.g1.model.PlayerPosition;
import mk.ukim.finki.wp.kol2023.g1.model.exceptions.InvalidPlayerIdException;
import mk.ukim.finki.wp.kol2023.g1.repository.PlayerRepository;
import mk.ukim.finki.wp.kol2023.g1.service.PlayerService;
import mk.ukim.finki.wp.kol2023.g1.service.TeamService;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class PlayerServiceImpl implements PlayerService {
   private final TeamService teamService;
   private final PlayerRepository playerRepository;

    public PlayerServiceImpl(TeamService teamService, PlayerRepository playerRepository) {
        this.teamService = teamService;
        this.playerRepository = playerRepository;
    }

    @Override
    public List<Player> listAllPlayers() {
        return playerRepository.findAll();
    }

    @Override
    public Player findById(Long id) {
        return playerRepository.findById(id).orElseThrow(InvalidPlayerIdException::new);
    }

    @Override
    public Player create(String name, String bio, Double pointsPerGame, PlayerPosition position, Long team) {
        return playerRepository.save(new Player(name,bio,pointsPerGame,position,teamService.findById(team)));
    }

    @Override
    public Player update(Long id, String name, String bio, Double pointsPerGame, PlayerPosition position, Long team) {
        Player p=this.findById(id);
        p.setName(name);
        p.setBio(bio);
        p.setPointsPerGame(pointsPerGame);
        p.setPosition(position);
        p.setTeam(teamService.findById(team));
        return playerRepository.save(p);
    }

    @Override
    public Player delete(Long id) {
        Player p=this.findById(id);
        playerRepository.delete(p);
        return p;
    }

    @Override
    public Player vote(Long id) {
        Player p=this.findById(id);
        p.setVotes(p.getVotes()+1);
        return playerRepository.save(p);
    }

    @Override
    public List<Player> listPlayersWithPointsLessThanAndPosition(Double pointsPerGame, PlayerPosition position) {
        if (pointsPerGame==null && position==null){
            return this.listAllPlayers();
        } else if (pointsPerGame!=null && position==null) {
           return playerRepository.findByPointsPerGameLessThan(pointsPerGame);
        }
     else if (pointsPerGame==null && position!=null) {
        return playerRepository.findByPosition(position);
    }
     else
         return playerRepository.findByPointsPerGameLessThanAndPosition(pointsPerGame,position);
    }
}
