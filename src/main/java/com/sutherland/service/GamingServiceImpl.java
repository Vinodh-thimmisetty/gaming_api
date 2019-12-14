package com.sutherland.service;

import com.sutherland.domain.GamingInfo;
import com.sutherland.entity.GamingEntity;
import com.sutherland.repository.GamingRepository;
import org.hibernate.jpa.QueryHints;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.sutherland.util.AppUtil.LIKE;
import static org.springframework.data.domain.Sort.Direction.fromString;
import static org.springframework.data.jpa.repository.query.QueryUtils.toOrders;

/**
 * @author vinodh kumar thimmisetty
 */
@Service
public class GamingServiceImpl implements GamingService {

    private GamingRepository gamingRepository;
    private EntityManager entityManager;

    public GamingServiceImpl(GamingRepository gamingRepository, EntityManager entityManager) {
        this.gamingRepository = gamingRepository;
        this.entityManager = entityManager;
    }

    @Override
    public List<GamingInfo> findAllGames(final Pageable pageable) {
        return this.gamingRepository.findAll(pageable).getContent()
                .stream().map(GamingInfo::new).distinct().collect(Collectors.toList());
    }

    @Override
    public List<GamingInfo> filterGames(final String title, final String platform, final String genre, final Double score, final String editors_choice, final String sortBy, final String sortDirection, final Integer pageNumber, final Integer pageSize) {
        final CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        final CriteriaQuery<GamingEntity> cq = cb.createQuery(GamingEntity.class);
        final Root<GamingEntity> $from = cq.from(GamingEntity.class);
        final Join<GamingEntity, String> $genres = $from.join("genre");
        List<Predicate> $filters = new ArrayList<>(5);
        if (!StringUtils.isEmpty(title)) {
            $filters.add(cb.like(cb.upper($from.get("title")), title.toUpperCase() + LIKE));
        }
        if (!StringUtils.isEmpty(platform)) {
            $filters.add(cb.like(cb.upper($from.get("platform")), platform.toUpperCase() + LIKE));
        }
        if (!StringUtils.isEmpty(genre)) {
            $filters.add(cb.like(cb.upper($genres), genre.toUpperCase() + LIKE));
        }
        if (score != null && score > -1) {
            $filters.add(cb.greaterThanOrEqualTo($from.get("score"), score));
        }
        if (!StringUtils.isEmpty(editors_choice)) {
            $filters.add(cb.like(cb.upper($from.get("editors_choice")), editors_choice.toUpperCase() + LIKE));
        }
        final CriteriaQuery<GamingEntity> $select = cq.select($from).where($filters.toArray(new Predicate[$filters.size()]));
        $select.orderBy(toOrders(Sort.by(fromString(sortDirection), sortBy), $from, cb));


        return entityManager
                .createQuery($select)
                .setHint(QueryHints.HINT_READONLY, true)
                .setFirstResult(pageNumber * pageSize)
                .setMaxResults(pageSize)
                .getResultList()
                .stream().map(GamingInfo::new)
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public GamingInfo createGame(final GamingInfo info) {
        final GamingEntity gEntity = new GamingEntity(info);
        final GamingEntity saved = gamingRepository.save(gEntity);
        return new GamingInfo(saved);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public GamingInfo updateGame(final Long gameId, final GamingInfo info) {
        final Optional<GamingEntity> byId = gamingRepository.findById(gameId);
        if (byId.isPresent()) {
            final GamingEntity gEntity = new GamingEntity(info);
            gEntity.setGameId(byId.get().getGameId());
            final GamingEntity saved = gamingRepository.save(gEntity);
            return new GamingInfo(saved);
        }
        return null;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public GamingInfo deleteGame(final Long gameId) {
        final Optional<GamingEntity> byId = gamingRepository.findById(gameId);
        if (byId.isPresent()) {
            final GamingEntity gamingEntity = byId.get();
            gamingRepository.delete(gamingEntity);
            return new GamingInfo(gamingEntity);
        }
        return null;
    }


}
