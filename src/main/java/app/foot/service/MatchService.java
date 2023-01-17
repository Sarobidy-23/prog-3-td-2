package app.foot.service;

import app.foot.model.Match;
import app.foot.model.exception.NotFoundException;
import app.foot.repository.MatchRepository;
import app.foot.repository.entity.MatchEntity;
import app.foot.repository.mapper.MatchMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class MatchService {
    private final MatchRepository repository;
    private final MatchMapper mapper;

    public List<Match> getMatches() {
        return repository.findAll().stream()
                .map(mapper::toDomain)
                .toList();
    }
    public MatchEntity getMatchesById(int id) {
        Optional<MatchEntity> match = repository.findById(id);
        if(match.isPresent()){
            return match.get();
        } else {
            throw new NotFoundException("Match.id."+id+".Not.Found");
        }
    }
}
