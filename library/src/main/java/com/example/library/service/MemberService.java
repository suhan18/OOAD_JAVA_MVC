package com.example.library.service;

import com.example.library.model.Member;
import com.example.library.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MemberService {
    private final MemberRepository repo;

    public MemberService(MemberRepository repo) {
        this.repo = repo;
    }

    public List<Member> getAllMembers() {
        return repo.findAll();
    }

    public Member addMember(Member member) {
        return repo.save(member);
    }

    public Optional<Member> updateMember(Long id, Member updated) {
        return repo.findById(id).map(member -> {
            member.setName(updated.getName());
            member.setEmail(updated.getEmail());
            return repo.save(member);
        });
    }

    public void deleteMember(Long id) {
        repo.deleteById(id);
    }
}
